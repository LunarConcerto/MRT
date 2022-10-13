package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.config.Property;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;

public class DlsiteOnseiElement implements Rule {

    @Override
    public void init(Configuration configuration) {
    }

    @Override
    public String getName() {
        return "Dlsite音声元素";
    }

    @Override
    public String getDescription() {
        return """
                    从Dlsite爬取与某作品相关的信息,
                 然后拼接在文件名上.
                    该规则会从文件原名中寻找RJ号,
                 然后用RJ号在DL站中找对应作品的信息.
                    若找不到RJ号的情况下,则会直接
                 在Dlsite中搜索原文件名,
                 当结果唯一时使用该结果.
               """;
    }

    @Override
    public RuleDefiner createDefiner() {
        return null;
    }

    @Override
    public RuleDefiner createDefiner(String serializedString) {
        return null;
    }

    static class DlsiteOnseiDefiner extends RuleDefiner {

        public DlsiteOnseiDefiner(Rule parentRule) {
            super(parentRule);
        }

        @Override
        public NameEditor createNameEditor() {
            return null;
        }

        @Override
        public String serialize() {
            return null;
        }

        @Override
        public String toSampleText() {
            return null;
        }

    }

}
