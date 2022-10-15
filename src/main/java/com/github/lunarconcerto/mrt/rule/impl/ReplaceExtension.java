package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.component.RenameTargetContainer;
import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class ReplaceExtension implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "替换拓展名";
    }

    @Override
    public String getDescription() {
        return """
                 一键替换选中文件的拓展名。
               """;
    }

    @Override
    public RuleDefiner createDefiner() {
        return new ReplaceExtensionDefiner(this);
    }

    @Override
    public RuleDefiner createDefiner(String serializedString) {
        return new ReplaceExtensionDefiner(this , serializedString);
    }

    static class ReplaceExtensionDefiner extends RuleDefiner {

        protected String extension = "" ;

        public ReplaceExtensionDefiner(Rule parentRule) {
            super(parentRule);

            init();
        }

        public ReplaceExtensionDefiner(Rule parentRule, String extension) {
            super(parentRule);
            this.extension = extension;

            init();
        }

        void init(){
            this.addLabel("更改拓展:");
            TextField textField = this.addTextField(extension, 80);
            textField.textProperty().addListener((observable, oldValue, newValue) -> extension = newValue);
        }

        @Override
        public NameEditor createNameEditor() {
            return new ReplaceExtensionEditor(extension);
        }

        @Override
        public String serialize() {
            return extension ;
        }

        @Override
        public String toSampleText() {
            return "将拓展名更改为:" + extension ;
        }

    }

    static class ReplaceExtensionEditor implements NameEditor {

        protected String extension ;

        public ReplaceExtensionEditor(String extension) {
            this.extension = extension;
        }

        @Override
        public void doEdit(RenameTargetContainer builder) {
            if (!extension.isEmpty()){
                builder.setTargetExtension(extension);
            }
        }
    }
}
