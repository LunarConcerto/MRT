package com.github.lunarconcerto.mrt.rule.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.task.FileRenameTargetWrapper;
import com.github.lunarconcerto.mrt.util.Texts;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.scene.text.Text;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PureReplace implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "替换文字";
    }

    @Override
    public Text[] getDescription() {
        return Texts.texts(Texts.textWithTabNewLine("将一段文字替换成另一段文字"));
    }

    @Override
    public RuleDefiningPane createDefiningPane() {
        return new PureReplaceDefiningPane(this);
    }

    @Override
    public RuleDefiningPane createDefiningPane(@NotNull String serializedString) {
        try {
            List<String> value = new ObjectMapper().readValue(serializedString, new TypeReference<>() {
            });
            return new PureReplaceDefiningPane(this, value.get(0), value.get(1));
        } catch (JsonProcessingException e) {
            return new PureReplaceDefiningPane(this);
        }
    }

    @Getter
    static class PureReplaceDefiningPane extends RuleDefiningPane {

        protected String oldText = "", newText = "";

        public PureReplaceDefiningPane(Rule parentRule) {
            super(parentRule);

            init();
        }

        public PureReplaceDefiningPane(Rule parentRule, String oldText, String newText) {
            super(parentRule);
            this.oldText = oldText;
            this.newText = newText;

            init();
        }

        protected void init() {
            MFXTextField textFieldOldText = this.addTextField(oldText, "要替换的文字");
            textFieldOldText.textProperty().addListener((observable, oldValue, newValue) -> oldText = newValue);
            MFXTextField textFieldNewText = this.addTextField(newText, "替换后的文字");
            textFieldNewText.textProperty().addListener((observable, oldValue, newValue) -> newText = newValue);
        }

        @Override
        public NameEditor createNameEditor() {
            return new PureReplaceEditor(oldText, newText);
        }

        @Override
        public String serialize() {
            try {
                return new ObjectMapper().writeValueAsString(List.of(oldText, newText));
            } catch (JsonProcessingException e) {
                throw new MRTRuntimeException(e);
            }
        }

        @Override
        public String toSampleText() {
            return oldText + "→" + newText;
        }

        public PureReplaceDefiningPane setOldText(String oldText) {
            this.oldText = oldText;
            return this;
        }

        public PureReplaceDefiningPane setNewText(String newText) {
            this.newText = newText;
            return this;
        }
    }

    static class PureReplaceEditor implements NameEditor {

        protected String oldText, newText;

        public PureReplaceEditor(String oldText, String newText) {
            this.oldText = oldText;
            this.newText = newText;
        }

        @Override
        public void doEdit(@NotNull FileRenameTargetWrapper builder) {
            builder.replace(oldText, newText);
        }

    }
}
