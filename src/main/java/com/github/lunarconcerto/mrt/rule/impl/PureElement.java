package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.task.FileRenameTargetWrapper;
import com.github.lunarconcerto.mrt.util.Texts;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.scene.text.Text;

import java.util.Arrays;

public class PureElement implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "可用选项";
    }

    @Override
    public Text[] getDescription() {
        return Texts.texts(
                Texts.textWithNewLine("可使用的选项如下"),
                Texts.textWithTabNewLine("①序号 : 该文件选择时的顺序，显示在已选列表中项目的左侧."),
                Texts.textWithTabNewLine("②原名 : 文件的原名.")
        );
    }

    @Override
    public RuleDefiningPane createDefiningPane() {
        return new PuleElementDefiningPane(this);
    }

    @Override
    public RuleDefiningPane createDefiningPane(String serializedString) {
        return new PuleElementDefiningPane(this, serializedString);
    }

    public enum Element {

        EMPTY("empty", "请选择"),

        INDEX("index", "序号"),

        SOURCE_NAME("source_name", "原名");

        final String serializeText, simpleText;

        Element(String serializeText, String simpleText) {
            this.serializeText = serializeText;
            this.simpleText = simpleText;
        }

        public static Element of(String serializeText) {
            return Arrays.stream(Element.values())
                    .filter(element -> element.getSerializeText().equals(serializeText))
                    .findFirst()
                    .orElse(EMPTY);
        }

        public String getSerializeText() {
            return serializeText;
        }

        public String getSimpleText() {
            return simpleText;
        }

        @Override
        public String toString() {
            return simpleText;
        }
    }

    public static class PuleElementDefiningPane extends RuleDefiningPane {

        protected Element element = Element.EMPTY;

        public PuleElementDefiningPane(Rule parentRule) {
            super(parentRule);

            init();
        }

        public PuleElementDefiningPane(Rule parentRule, String serializedString) {
            super(parentRule);
            element = Element.of(serializedString);

            init();
        }

        protected void init() {
            MFXLegacyComboBox<Element> box = this.addChoiceBox(Element.values());

            box.getSelectionModel().select(element);
            box.valueProperty().addListener((observable, oldValue, newValue) -> element = newValue);
        }

        @Override
        public NameEditor createNameEditor() {
            return new PureElementNameEditor(element);
        }

        @Override
        public String serialize() {
            return element.getSerializeText();
        }

        @Override
        public String toSampleText() {
            return element.getSimpleText();
        }

    }

    static class PureElementNameEditor implements NameEditor {

        Element element;

        public PureElementNameEditor(Element element) {
            this.element = element;
        }

        @Override
        public void doEdit(FileRenameTargetWrapper builder) {
            switch (element) {
                case INDEX -> builder.append(builder.getIndex() + 1);
                case SOURCE_NAME -> builder.append(builder.getTargetSourceName());
                default -> {
                    /* do nothing */
                }
            }
        }


    }

}
