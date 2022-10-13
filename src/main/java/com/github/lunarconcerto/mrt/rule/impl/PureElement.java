package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.component.RenameTargetContainer;
import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.RuleType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;

import java.util.Arrays;
import java.util.Optional;

public class PureElement implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "可用元素" ;
    }

    @Override
    public RuleType getType() {
        return RuleType.FILLING ;
    }

    @Override
    public String getDescription() {
        return """
                使用固定元素:
                    ①序号 : 该文件选择时的顺序，显示在已选列表中项目的左侧.
                    ②原名 : 文件的原名.
               """;
    }

    @Override
    public RuleDefiner createDefiner() {
        return new PureElement.PuleElementDefiner(this);
    }

    @Override
    public RuleDefiner createDefiner(String serializedString) {
        return new PureElement.PuleElementDefiner(this , serializedString) ;
    }

    public static class PuleElementDefiner extends RuleDefiner {

        protected Element element = Element.EMPTY ;

        public PuleElementDefiner(Rule parentRule) {
            super(parentRule);

            init();
        }

        public PuleElementDefiner(Rule parentRule, String serializedString) {
            super(parentRule);
            element = Element.of(serializedString);

            init();
        }

        void init(){
            this.addLabel("选择元素:");
            ChoiceBox<Element> box = this.addChoiceBox(100 , Element.values());

            box.getSelectionModel().select(element);
            box.valueProperty().addListener((observable, oldValue, newValue) -> element = newValue);
        }

        @Override
        public NameEditor createNameEditor() {
            return new PureElementNameEditor(element);
        }

        @Override
        public String serialize() {
            return element.getSerializeText() ;
        }

        @Override
        public String toSampleText() {
            return element.getSimpleText();
        }

    }

    static class PureElementNameEditor implements NameEditor {

        Element element ;

        public PureElementNameEditor(Element element) {
            this.element = element;
        }

        @Override
        public void doEdit(RenameTargetContainer builder) {
            switch (element){
                case INDEX -> builder.append(builder.getIndex()+1);
                case SOURCE_NAME -> builder.append(builder.getTargetSourceName());
                default -> {
                    /* do nothing */
                }
            }
        }


    }

    public enum Element {

        EMPTY("empty" , "无选择"),

        INDEX("index", "序号"),

        SOURCE_NAME("source_name", "原名");

        final String serializeText , simpleText;

        Element(String serializeText, String simpleText) {
            this.serializeText = serializeText;
            this.simpleText = simpleText;
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

        public static Element of(String serializeText){
            return Arrays.stream(Element.values())
                    .filter(element -> element.getSerializeText().equals(serializeText))
                    .findFirst()
                    .orElse(EMPTY);
        }
    }

}
