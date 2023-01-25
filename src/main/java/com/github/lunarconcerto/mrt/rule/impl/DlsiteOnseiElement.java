package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.controller.WindowManager;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.task.FileRenameTargetWrapper;
import com.github.lunarconcerto.mrt.util.Logger;
import com.github.lunarconcerto.mrt.util.Texts;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DlsiteOnseiElement implements Rule {

    public static final String NAME = "dlsite_onsei";

    @Override
    public void init(Configuration configuration) {
    }

    @Override
    public String getName() {
        return "Dlsite音声可选项";
    }

    /*
    * """
                     从Dlsite爬取与某作品相关的信息,
                  然后拼接在文件名上.
                     该规则会从文件原名中寻找RJ号,
                  然后用RJ号在DL站中找对应作品的信息.
                     若找不到RJ号的情况下,则会直接
                  在Dlsite中搜索原文件名,
                  当结果唯一时使用该结果.
                """
    * */

    @Override
    public Text[] getDescription() {
        return Texts.texts(
                Texts.textWithTabNewLine("该规则可以从Dlsite爬取与某作品相关的信息,然后拼接在文件名上."),
                Texts.textWithTabNewLine("原理是从文件原名中匹配RJ号,然后用RJ号在Dlsite中找对应作品的信息."),
                Texts.textWithTabNewLine("若找不到RJ号的情况下,则直接在Dlsite中搜索原文件名,在结果唯一时使用该结果.")
        );
    }

    @Override
    public RuleDefiningPane createDefiningPane() {
        return new DlsiteOnseiDefiningPane(this);
    }

    @Override
    public RuleDefiningPane createDefiningPane(String serializedString) {
        return new DlsiteOnseiDefiningPane(this, Item.of(serializedString));
    }

    enum Item {

        NONE("请选择", "none"),

        PRODUCT_NAME("作品名", "product_name"),

        RJ("RJ号", "rj"),

        MAKER_NAME("社团名", "maker_name", "サークル名", "作者"),

        CV_NAME("声优", "cv_name", "声優"),

        DATE("贩售日", "date", "販売日");

        final String name, text;

        final String[] tags;

        Item(String name, String text, String... tags) {
            this.name = name;
            this.text = text;
            this.tags = tags;
        }

        static Item of(String text) {
            Optional<Item> optional = Arrays.stream(Item.values())
                    .filter(item -> item.text.equals(text))
                    .findFirst();
            return optional.orElse(Item.NONE);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static class DlsiteOnseiDefiningPane extends RuleDefiningPane {

        protected Item item = Item.NONE;

        public DlsiteOnseiDefiningPane(Rule parentRule) {
            super(parentRule);

            init();
        }

        public DlsiteOnseiDefiningPane(Rule parentRule, Item item) {
            super(parentRule);
            this.item = item;

            init();
        }

        protected void init() {
            MFXLegacyComboBox<Item> box = this.addChoiceBox(Item.values());

            box.getSelectionModel().select(item);
            box.valueProperty().addListener((observable, oldValue, newValue) -> item = newValue);
        }

        @Override
        public NameEditor createNameEditor() {
            return new DlsiteOnseiEditor(item);
        }

        @Override
        public String serialize() {
            return item.text;
        }

        @Override
        public String toSampleText() {
            return item.name;
        }

    }

    static class DlsiteOnseiEditor implements NameEditor {

        static final String DL_PRODUCT_URL = "https://www.dlsite.com/maniax/work/=/product_id/%s.html";
        static final String DL_SEARCH_URL = "https://www.dlsite.com/maniax/fsr/=/keyword/%s/and/per_page/10/from/fs.header";

        static final String SKIP = "skip";
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        protected Item item;

        public DlsiteOnseiEditor(Item item) {
            this.item = item;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void doEdit(@NotNull FileRenameTargetWrapper builder) {
            Object o = builder.getCache().get(NAME);
            if (o != null) {
                HashMap<Item, String> map = (HashMap<Item, String>) o;
                if (!SKIP.equals(map.get(Item.NONE))) {
                    builder.append(map.get(item));
                } else {
                    builder.skip();
                }
            } else {
                HashMap<Item, String> information = getInformation(builder.getTargetSourceName());
                if (!SKIP.equals(information.get(Item.NONE))) {
                    builder.append(information.get(item));
                    builder.getCache().put(NAME, information);
                } else {
                    builder.skip();
                }
            }
        }

        public HashMap<Item, String> getInformation(String sourceName) {
            Optional<String> rj = checkIncludeRJ(sourceName);
            return rj.map(this::getInformationFromRJ).orElseGet(() -> getInformationUseSourceName(sourceName));
        }

        private @NotNull HashMap<Item, String> getInformationFromRJ(@NotNull String rj) {
            HashMap<Item, String> result = createNewMap();
            result.put(Item.RJ, rj);
            try {
                Document document = Jsoup.connect(DL_PRODUCT_URL.formatted(rj)).get();
                return getInformationFromDocument(document, result);
            } catch (IOException e) {
                WindowManager.showError("连接失败", "无法连接至Dlsite网站，进程已停止。请检查你的代理设置。");
                return createSkipMap("连接失败", rj);
            }
        }

        private @NotNull HashMap<Item, String> getInformationUseSourceName(String sourceName) {
            String url = DL_SEARCH_URL.formatted(sourceName);
            try {
                Document document = Jsoup.connect(url).get();

                Elements elements = document.getElementsByTag("div")
                        .attr("id", "search_result_img_box_inner type_exclusive_01 ")
                        .attr("class", "multiline_truncate")
                        .select("a[href][title]");

                if (elements.size() == 0) {
                    return createSkipMap("没有找到匹配结果", sourceName);
                } else if (elements.size() == 1) {
                    Optional<String> rj = checkIncludeRJ(elements.get(0).attr("href"));
                    return rj.map(this::getInformationFromRJ).orElseGet(() -> createSkipMap("", ""));
                } else if (elements.size() < 6) {
                    List<ProductInfo> infos = new ArrayList<>();
                    for (Element element : elements) {
                        String productURL = element.attr("href");
                        String productTitle = element.attr("title");
                        ProductInfo info = new ProductInfo();
                        info.setTitle(productTitle);
                        info.setUrl(productURL);
                        info.setButtonType(new ButtonType(productTitle));
                        infos.add(info);
                    }

                    ButtonType type = WindowManager.showCommandLinks("找到多个作品",
                            "请选择一个可能与是该项目:" + sourceName + "的链接", infos.stream()
                                    .map(ProductInfo::getButtonType)
                                    .toList()
                                    .toArray(new ButtonType[infos.size()])
                    );

                    if (type == ButtonType.CLOSE) {
                        return createSkipMap("用户取消", sourceName);
                    } else {
                        Optional<ProductInfo> info = infos.stream()
                                .filter(productInfo -> productInfo.getButtonType().equals(type))
                                .findFirst();
                        if (info.isPresent()) {
                            Optional<String> rj = checkIncludeRJ(info.get().getUrl());
                            return rj.map(this::getInformationFromRJ).orElseGet(() -> createSkipMap("选择的作品无RJ", sourceName));
                        } else {
                            return createSkipMap("用户取消", sourceName);
                        }
                    }

                } else {
                    return createSkipMap("结果过多", sourceName);
                }
            } catch (IOException e) {
                return createSkipMap("连接失败", sourceName);
            }
        }

        HashMap<Item, String> getInformationFromDocument(@NotNull Document document, @NotNull HashMap<Item, String> map) {
            /* 获取作品名 */
            map.put(Item.PRODUCT_NAME, document.getElementsByTag("h1").attr("id", "work_name").text());

            /* 获取其他项目信息 */
            Elements tableContent = document.getElementsByTag("table")
                    .attr("cellspacing", "0")
                    .attr("id", "work_outline")
                    .select("tr");

            Arrays.stream(Item.values()).forEachOrdered(item -> tableContent.stream()
                    .filter(element -> item.tags != null && Arrays.stream(item.tags)
                            .anyMatch(s -> s.equals(element.select("th").text())))
                    .findFirst()
                    .ifPresent(element -> map.put(item, element.select("td").text())));

            /* 格式化日期 */
            String date = map.get(Item.DATE);
            if (!date.isEmpty()) {
                map.put(Item.DATE, dateFormat(date));
            }

            return map;
        }

        /**
         * 初始化一个包含所有元素且对应值为空值的新表
         */
        private @NotNull HashMap<Item, String> createNewMap() {
            return Arrays.stream(Item.values()).collect(Collectors.toMap(value -> value, value -> "", (a, b) -> b, HashMap::new));
        }

        private @NotNull HashMap<Item, String> createSkipMap(String message, String sourceName) {
            HashMap<Item, String> map = createNewMap();
            map.put(Item.NONE, SKIP);
            Logger.info("跳过了名为:" + sourceName + "的项目 , 因为" + message);
            return map;
        }

        private Optional<String> checkIncludeRJ(String string) {
            Pattern compile = Pattern.compile("RJ0\\d{1,7}|RJ\\d{1,6}");
            Matcher matcher = compile.matcher(string);
            if (matcher.find()) {
                return Optional.of(matcher.group());
            } else {
                return Optional.empty();
            }
        }

        private @NotNull String dateFormat(String dateString) {
            StringBuilder builder = new StringBuilder();
            TemporalAccessor accessor;
            try {
                accessor = formatter.parse(dateString);
            } catch (DateTimeParseException e) {
                accessor = formatter.parse(dateString.substring(0, dateString.indexOf("日") + 1));
            }
            int i = accessor.get(ChronoField.YEAR) % 100;
            builder.append(i);
            int month = accessor.get(ChronoField.MONTH_OF_YEAR);
            if (month < 10) {
                builder.append("0").append(month);
            } else {
                builder.append(month);
            }
            builder.append(accessor.get(ChronoField.DAY_OF_MONTH));
            return builder.toString();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class ProductInfo {

        String url, title;

        ButtonType buttonType;

    }

}
