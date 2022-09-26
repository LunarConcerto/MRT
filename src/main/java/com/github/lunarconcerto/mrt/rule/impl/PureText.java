package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.component.FillingObjectContainer;
import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.RuleType;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

public class PureText implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "输入字符";
    }

    @Override
    public String getDescription() {
        return "直接拼接一段恒定不变的文字.";
    }

    @Override
    public RuleType getType() {
        return RuleType.FILLING ;
    }

    @Override
    public RuleDefiner createDefiner() {
        return new PureTextDefiner();
    }

    @Override
    public RuleDefiner createDefiner(String serializedString) {
        return new PureTextDefiner(serializedString);
    }

    static class PureTextDefiner extends RuleDefiner {

        protected String text = "";

        protected TextField textField ;

        public PureTextDefiner() {
            init();
        }

        public PureTextDefiner(String text) {
            this.text = text;

            init();
        }

        void init(){
            this.addLabel("拼接字符: ");
            textField = this.addTextField();
        }

        @Override
        public NameEditor createNameEditor() {
            return new PureTextNameEditor(text);
        }

        @Override
        public String serialize() {
            return text ;
        }

        public String getText() {
            return text;
        }

        public PureTextDefiner setText(String text) {
            this.text = text;
            return this;
        }
    }

    record PureTextNameEditor(String text) implements NameEditor {

        @Override
            public void doEdit(@NotNull FillingObjectContainer filler) {
                filler.append(text);
            }

    }

}
