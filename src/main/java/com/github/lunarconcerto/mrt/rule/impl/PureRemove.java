package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.component.RenameTargetContainer;
import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

public class PureRemove implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "移除字符";
    }

    @Override
    public String getDescription() {
        return """
                将文件名中的匹配字符全部移除。
                (注意:作用的对象为该规则为止的
                        被组成的新名字)
               """;
    }

    @Override
    public RuleDefiner createDefiner() {
        return new PureRemoveDefiner(this);
    }

    @Override
    public RuleDefiner createDefiner(String serializedString) {
        return new PureRemoveDefiner(this , serializedString);
    }

    static class PureRemoveDefiner extends RuleDefiner {

        protected String targetText = "" ;

        public PureRemoveDefiner(Rule parentRule) {
            super(parentRule);
            init();
        }

        public PureRemoveDefiner(Rule parentRule, String targetText) {
            super(parentRule);
            this.targetText = targetText;
            init();
        }

        void init(){
            this.addLabel("删除所有:");
            TextField textField = this.addTextField(targetText, 100);
            textField.textProperty().addListener((observable, oldValue, newValue) -> targetText = newValue);
        }

        @Override
        public NameEditor createNameEditor() {
            return new PureRemoveEditor(targetText);
        }

        @Override
        public String serialize() {
            return this.targetText ;
        }

        @Override
        public String toSampleText() {
            return "删除\"" + targetText + "\"";
        }
    }

    static class PureRemoveEditor implements NameEditor {

        protected String targetText ;

        public PureRemoveEditor(String targetText) {
            this.targetText = targetText;
        }

        @Override
        public void doEdit(@NotNull RenameTargetContainer builder) {
            builder.replace(targetText , "");
        }

    }

}
