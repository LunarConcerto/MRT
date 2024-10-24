package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.task.FileRenameTargetWrapper;
import com.github.lunarconcerto.mrt.util.Texts;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ReplaceExtension implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "替换拓展名";
    }

    @Override
    public Text[] getDescription() {
        return Texts.texts(Texts.textWithTabNewLine(" 一键替换选中文件的拓展名."));
    }

    @Override
    public RuleDefiningPane createDefiningPane() {
        return new ReplaceExtensionDefiningPane(this);
    }

    @Override
    public RuleDefiningPane createDefiningPane(String serializedString) {
        return new ReplaceExtensionDefiningPane(this, serializedString);
    }

    static class ReplaceExtensionDefiningPane extends RuleDefiningPane {

        protected String extension = "";

        public ReplaceExtensionDefiningPane(Rule parentRule) {
            super(parentRule);

            init();
        }

        public ReplaceExtensionDefiningPane(Rule parentRule, String extension) {
            super(parentRule);
            this.extension = extension;

            init();
        }

        protected void init() {
            TextField textField = this.addTextField(extension, "替换后的拓展名");
            textField.textProperty().addListener((observable, oldValue, newValue) -> extension = newValue);
        }

        @Override
        public NameEditor createNameEditor() {
            return new ReplaceExtensionEditor(extension);
        }

        @Override
        public String serialize() {
            return extension;
        }

        @Override
        public String toSampleText() {
            return "将拓展名更改为:" + extension;
        }

    }

    static class ReplaceExtensionEditor implements NameEditor {

        protected String extension;

        public ReplaceExtensionEditor(String extension) {
            this.extension = extension;
        }

        @Override
        public void doEdit(FileRenameTargetWrapper builder) {
            if (!extension.isEmpty()) {
                builder.setTargetExtension(extension);
            }
        }
    }
}
