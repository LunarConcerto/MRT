package com.github.lunarconcerto.mrt.rule.impl;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.RuleType;

public class PureElement implements Rule {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public String getName() {
        return "选择元素" ;
    }

    @Override
    public RuleType getType() {
        return RuleType.FILLING ;
    }

    @Override
    public String getDescription() {
        return "使用一些固定的要素，如文件的原名、选择的序号等。";
    }

    @Override
    public RuleDefiner createDefiner() {
        return new RuleDefiner.EmptyRuleDefiner();
    }

    @Override
    public RuleDefiner createDefiner(String serializedString) {
        return new RuleDefiner.EmptyRuleDefiner() ;
    }
}
