package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.task.FileRenameTargetWrapper;
import com.github.lunarconcerto.mrt.util.Texts;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

public class PureText implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "添加文字";
    }

    @Override
    public Text[] getDescription() {
        return Texts.texts(Texts.textWithTabNewLine("拼接一段文字."));
    }

    @Override
    public RuleDefiningPane createDefiningPane() {
        return new PureTextDefiningPane(this);
    }

    @Override
    public RuleDefiningPane createDefiningPane(String serializedString) {
        return new PureTextDefiningPane(this, serializedString);
    }

    static class PureTextDefiningPane extends RuleDefiningPane {

        protected String text = "";

        protected MFXTextField textField;

        public PureTextDefiningPane(Rule parentRule) {
            super(parentRule);

            init();
        }

        public PureTextDefiningPane(Rule parentRule, String text) {
            super(parentRule);
            this.text = text;

            init();
        }

        protected void init() {
            textField = this.addTextField(text, "要添加的文字");
            textField.textProperty().addListener((observable, oldValue, newValue) -> text = newValue);
            if (!text.isEmpty()) {
                textField.setText(text);
            }
        }

        @Override
        public NameEditor createNameEditor() {
            return new PureTextNameEditor(text);
        }

        @Override
        public String serialize() {
            return text;
        }

        @Override
        public String toSampleText() {
            return text;
        }

        public String getText() {
            return text;
        }

        public PureTextDefiningPane setText(String text) {
            this.text = text;
            return this;
        }
    }

    record PureTextNameEditor(String text) implements NameEditor {

        @Override
        public void doEdit(@NotNull FileRenameTargetWrapper builder) {
            builder.append(text);
        }

    }

}
