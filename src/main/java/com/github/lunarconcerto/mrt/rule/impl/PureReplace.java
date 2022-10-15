package com.github.lunarconcerto.mrt.rule.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lunarconcerto.mrt.component.RenameTargetContainer;
import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import javafx.scene.control.TextField;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PureReplace implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "替换字符" ;
    }

    @Override
    public String getDescription() {
        return "将一段字符替换成另一段字符";
    }

    @Override
    public RuleDefiner createDefiner() {
        return new PureReplaceDefiner(this);
    }

    @Override
    public RuleDefiner createDefiner(@NotNull String serializedString) {
        try {
            List<String> value = new ObjectMapper().readValue(serializedString, new TypeReference<>() {});
            return new PureReplaceDefiner(this, value.get(0), value.get(1));
        } catch (JsonProcessingException e) {
            return new PureReplaceDefiner(this);
        }
    }

    @Getter
    static class PureReplaceDefiner extends RuleDefiner{

        protected String oldText = "" , newText = "" ;
        public PureReplaceDefiner(Rule parentRule) {
            super(parentRule);

            init();
        }

        public PureReplaceDefiner(Rule parentRule, String oldText, String newText) {
            super(parentRule);
            this.oldText = oldText;
            this.newText = newText;

            init();
        }

        void init(){
            this.addLabel("将");
            TextField textFieldOldText = this.addTextField(80);
            textFieldOldText.textProperty().addListener((observable, oldValue, newValue) -> oldText = newValue);
            this.addLabel("替换为");
            TextField textFieldNewText = this.addTextField(80);
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
            return oldText + "→" + newText ;
        }

        public PureReplaceDefiner setOldText(String oldText) {
            this.oldText = oldText;
            return this;
        }

        public PureReplaceDefiner setNewText(String newText) {
            this.newText = newText;
            return this;
        }
    }

    static class PureReplaceEditor implements NameEditor {

        protected String oldText , newText ;

        public PureReplaceEditor(String oldText, String newText) {
            this.oldText = oldText;
            this.newText = newText;
        }

        @Override
        public void doEdit(@NotNull RenameTargetContainer builder) {
            builder.replace(oldText , newText);
        }

    }
}
