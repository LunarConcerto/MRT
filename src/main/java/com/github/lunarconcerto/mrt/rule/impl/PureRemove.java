package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.task.FileRenameTargetWrapper;
import com.github.lunarconcerto.mrt.util.Texts;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

public class PureRemove implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "删除文字";
    }

    @Override
    public Text[] getDescription() {
        return Texts.texts(
                Texts.textWithTabNewLine("将文件名中的匹配的文字全部移除."),
                Texts.textWithTabNewLine("(注意:作用的对象为该规则为止的被组成的新名字)")
        );
    }

    @Override
    public RuleDefiningPane createDefiningPane() {
        return new PureRemoveDefiningPane(this);
    }

    @Override
    public RuleDefiningPane createDefiningPane(String serializedString) {
        return new PureRemoveDefiningPane(this, serializedString);
    }

    static class PureRemoveDefiningPane extends RuleDefiningPane {

        protected String targetText = "";

        public PureRemoveDefiningPane(Rule parentRule) {
            super(parentRule);
            init();
        }

        public PureRemoveDefiningPane(Rule parentRule, String targetText) {
            super(parentRule);
            this.targetText = targetText;
            init();
        }

        protected void init() {
            TextField textField = this.addTextField(targetText, "要删除的文字");
            textField.textProperty().addListener((observable, oldValue, newValue) -> targetText = newValue);
        }

        @Override
        public NameEditor createNameEditor() {
            return new PureRemoveEditor(targetText);
        }

        @Override
        public String serialize() {
            return this.targetText;
        }

        @Override
        public String toSampleText() {
            return "删除\"" + targetText + "\"";
        }
    }

    static class PureRemoveEditor implements NameEditor {

        protected String targetText;

        public PureRemoveEditor(String targetText) {
            this.targetText = targetText;
        }

        @Override
        public void doEdit(@NotNull FileRenameTargetWrapper builder) {
            builder.replace(targetText, "");
        }

    }

}
