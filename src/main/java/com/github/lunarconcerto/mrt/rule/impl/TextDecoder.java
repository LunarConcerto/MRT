package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.task.FileRenameTargetWrapper;
import com.github.lunarconcerto.mrt.util.Texts;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class TextDecoder implements Rule {


    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "乱码解除";
    }

    @Override
    public Text[] getDescription() {
        return Texts.texts(
                Texts.textWithTabNewLine("解除常见的日文乱码."),
                Texts.textWithTabNewLine("注意: 如果只需要解除文件名的乱码, 那么在编辑规则时, 需先添加一个规则<可用选项-原名>, 若只含该规则会导致得到一个空的新文件名."),
                Texts.text("关于选项"), Texts.text("强制", Color.RED), Texts.textWithNewLine("的说明:"),
                Texts.textWithTab("若不开启强制,则会先检测是否是乱码(检测不一定百分百准确),若是乱码时再进行解除,这是因为"),
                Texts.text("对正常的非乱码文字执行解码会使正常文字也变成乱码.", Color.RED)
        );
    }

    @Override
    public RuleDefiningPane createDefiningPane() {
        return new TextDecoderDefiningPane(this, false);
    }

    @Override
    public RuleDefiningPane createDefiningPane(String serializedString) {
        return new TextDecoderDefiningPane(this, Boolean.parseBoolean(serializedString));
    }

    public static class TextDecoderDefiningPane extends RuleDefiningPane {

        protected boolean force;

        public TextDecoderDefiningPane(Rule parentRule, boolean force) {
            super(parentRule);
            this.force = force;
            init();
        }

        @Override
        protected void init() {
            addToggleButton("强制").selectedProperty().addListener((observable, oldValue, newValue) -> force = newValue);
        }

        @Override
        public NameEditor createNameEditor() {
            return new TextDecoderNameEditor(force);
        }

        @Override
        public String serialize() {
            return String.valueOf(force);
        }

        @Override
        public String toSampleText() {
            return "decode";
        }
    }

    public static class TextDecoderNameEditor implements NameEditor {

        protected boolean force;

        public TextDecoderNameEditor(boolean force) {
            this.force = force;
        }

        @Override
        public void doEdit(@NotNull FileRenameTargetWrapper builder) {
            String text = builder.getTextBuilder().toString();
            if (force || isShiftJisMistakenCode(text)) {
                builder.setTextBuilder(new StringBuilder(decode(text)));
            }
        }

        public boolean isShiftJisMistakenCode(String text) {
            return decode3Times(text).contains("????");
        }

        public String decode3Times(String source) {
            String result = source;
            for (int i = 0; i < 3; i++) {
                result = decode(result);
            }
            return result;
        }

        public String decode(String encoded) {
            try {
                String encode = URLEncoder.encode(encoded, "gbk");
                return URLDecoder.decode(encode, "shift-jis");

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

        }

    }

}
