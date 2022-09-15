package com.github.lunarconcerto.mrt.rule.impl.dlsite.onsei;

import com.github.lunarconcerto.mrt.core.MRTApp;
import com.github.lunarconcerto.mrt.rule.AbstractRule;
import com.github.lunarconcerto.mrt.rule.RenameResult;
import com.github.lunarconcerto.mrt.util.FileNode;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DlsiteOnseiRule extends AbstractRule {

    OnseiSettingsController controller ;

    public static final String prefix = "dlsite-onsei-";

    public static final String URL_BY_RJ_NUMBER = "https://www.dlsite.com/maniax/work/=/product_id/%s.html" ;
    public static final String URL_BY_NAME_SEARCH = "https://www.dlsite.com/maniax/fsr/=/language/jp/sex_category%5B0%5D/male/keyword/";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    public DlsiteOnseiRule() {
        this.name = "Dlsite 音声整理规则" ;
    }

    @Override
    public AnchorPane getSettingPanel() throws IOException {
        if (controller==null){
            controller = new OnseiSettingsController();
        }

        return controller.loadFXML();
    }

    @Override
    public void saveSettings(@NotNull Properties properties) {
        List<Column> columnList = controller.columnList;

        int i = 1 ;
        for (Column column : columnList) {
            String columnPrefix = column.getPrefix().getText();
            if (columnPrefix !=null){
                properties.setProperty(prefix + "prefix" + i , columnPrefix);
            }

            Element element = column.getElement().getSelectionModel().getSelectedItem();
            properties.setProperty(prefix+"element"+i , element.getId());

            String columnSuffix = column.getSuffix().getText();
            if (columnSuffix!=null){
                properties.setProperty(prefix + "suffix" + i , columnSuffix);
            }

            i++ ;
        }
    }

    @Override
    public void loadSettings(Properties properties) {
        List<Column> columnList = controller.columnList;

        int i = 1 ;
        for (Column column : columnList) {
            String columnPrefix = properties.getProperty(prefix + "prefix" + i);
            if (columnPrefix!=null){
                column.getPrefix().setText(columnPrefix);
            }

            String element = properties.getProperty(prefix + "element" + i);
            if (element!=null){
                column.getElement().getSelectionModel().select(Element.of(element));
            }

            String columnSuffix = properties.getProperty(prefix + "suffix" + i);
            if (columnSuffix!=null){
                column.getSuffix().setText(columnSuffix);
            }

            i++;
        }

        controller.createSamples();
    }

    @Override
    public List<RenameResult> run(@NotNull List<FileNode> fileNodeList , @NotNull ProgressBar bar) {
        double increment = 1.0 / fileNodeList.size() , progress = increment ;
        List<RenameResult> results = new ArrayList<>();

        MRTApp.printToUiLogger("现在开始爬取信息");
        long startTimes = System.currentTimeMillis();

        for (FileNode fileNode : fileNodeList) {
            List<Information> info = getInfo(fileNode);

            if (info.isEmpty()){
                continue;
            }

            results.add(buildRenameResult(info , fileNode));

            double finalProgress = progress;
            Platform.runLater(() -> {
                bar.setProgress(finalProgress);
            });

            progress += increment;
        }

        long endTimes = System.currentTimeMillis();
        MRTApp.printToUiLogger("已全部爬取完成,用时%s秒".formatted((endTimes-startTimes)/1000));

        return results ;
    }

    @Contract("_, _ -> new")
    private @NotNull RenameResult buildRenameResult(List<Information> info , FileNode node){
        StringBuilder builder = new StringBuilder();
        for (Column column : controller.columnList) {
            Optional<String> stringOptional = match(info, column.getElement().getSelectionModel().getSelectedItem());
            if (stringOptional.isPresent()){
                builder.append(column.getPrefix().getText());
                builder.append(stringOptional.get());
                builder.append(column.getSuffix().getText());
            }
        }
        return new RenameResult(node , builder.toString());
    }

    @Contract(pure = true)
    private @NotNull Optional<String> match(@NotNull List<Information> info , Element element){
        return info.stream()
                .filter(information -> information.type == element)
                .findFirst().map(information -> information.text);
    }

    private @NotNull List<Information> getInfo(@NotNull FileNode fileNode){
        String name = fileNode.getName();
        Optional<String> rj = checkIncludeRJ(name);
        return rj.map(this::getInfoByRJNumber).orElseGet(() -> getInfoByFileName(name));

    }

    private @NotNull List<Information> getInfoByFileName(String fileName){
        try {
            String url = URL_BY_NAME_SEARCH + URLEncoder.encode(fileName , String.valueOf(StandardCharsets.UTF_8));

            Document document = Jsoup.connect(url).get();

            Elements searchResult = document.select("dt[class]");
            List<String> id = searchResult.eachAttr("id");

            if (id.isEmpty()){
                MRTApp.printToUiLogger("未能搜索到结果...");
            }else if (id.size()==1){
                Optional<String> rj = checkIncludeRJ(id.get(0));
                if (rj.isPresent()){
                    return getInfoByRJNumber(rj.get());
                }
            }else {
                MRTApp.printToUiLogger("搜索到多个结果...");
            }
        } catch (IOException e) {
            netExceptionHandler(e);
        }
        return new ArrayList<>();
    }

    private @NotNull List<Information> getInfoByRJNumber(String rjNumber){
        String url = URL_BY_RJ_NUMBER.formatted(rjNumber);
        List<Information> info = new ArrayList<>(5);
        info.add(new Information(Element.RJ , rjNumber));

        try {
            Document document = Jsoup.connect(url).get();
            getInformation(info, document);
        } catch (IOException e) {
            netExceptionHandler(e);
        }

        return info ;
    }


    private void getInformation(@NotNull List<Information> info , @NotNull Document document){
        /* 作品名 */
        String PRODUCT_NAME = document.select("[id=work_name]").text();
        info.add(new Information(Element.NAME , PRODUCT_NAME));
        /* 社团名 */
        String MAKER_NAME = document.select("[class=maker_name]").text();
        info.add(new Information(Element.AUTHOR , MAKER_NAME));
        /* CV名 */
        Optional<String> cvName = findTrTagResource(document, Element.CV.getName());
        cvName.ifPresent(s -> {
            info.add(new Information(Element.CV , s));
        });
        /* 贩售日 */
        Optional<String> date = findTrTagResource(document, Element.DATE.getName());
        date.ifPresent(s -> {
            info.add(new Information(Element.DATE , dateFormat(s)));
        });

    }

    private @NotNull String dateFormat(String dateString){
        StringBuilder builder = new StringBuilder();
        TemporalAccessor accessor ;
        try {
            accessor = formatter.parse(dateString);
        }catch (DateTimeParseException e){
            accessor = formatter.parse(dateString.substring(0 , dateString.indexOf("日")+1));
        }
        int i = accessor.get(ChronoField.YEAR) % 100;
        builder.append(i);
        int month = accessor.get(ChronoField.MONTH_OF_YEAR);
        if (month<10){
            builder.append("0").append(month);
        }else {
            builder.append(month);
        }
        builder.append(accessor.get(ChronoField.DAY_OF_MONTH));
        return builder.toString() ;
    }

    private void netExceptionHandler(IOException e){
        if (e instanceof ConnectException){
            MRTApp.printToUiLogger("连接被拒绝，请检查代理是否开启，代理地址及端口填写是否正确。");
        }else {
            e.printStackTrace();
        }
    }

    private Optional<String> checkIncludeRJ(String string){
        Pattern compile = Pattern.compile("RJ[0-9]{1,6}");
        Matcher matcher = compile.matcher(string);
        if (matcher.find()){
            return Optional.of(matcher.group()) ;
        }else {
            return Optional.empty() ;
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private @NotNull Optional<String> findTrTagResource(@NotNull Document document , String tagName){
        return Optional.of(document.select("tr").stream()
                .filter(element -> {
                    Elements matchingText = element.getElementsMatchingText(tagName);
                    return !matchingText.isEmpty();
                }).findFirst()
                .get()
                .select("td")
                .text()
        );
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Information{

        private Element type ;

        private String text ;

        public void ifPresent(Runnable runnable){
            if (text!=null && !text.equals("")){
                runnable.run();
            }
        }

    }

}
