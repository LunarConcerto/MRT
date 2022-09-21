package com.github.lunarconcerto.mrt.rule;

public interface Rule {

    String getName();

    RuleType getType();

    RuleDefiner getDefiner();

}
