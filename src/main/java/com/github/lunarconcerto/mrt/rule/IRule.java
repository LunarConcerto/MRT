package com.github.lunarconcerto.mrt.rule;

public interface IRule {

    String getName();

    RuleType getType();

    RuleDefiner getDefiner();

}
