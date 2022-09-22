package com.github.lunarconcerto.mrt.rule;

public interface Rule {

    void init();

    /**
     * 显示在UI上的名字
     */
    String getName();

    /**
     * 定义该规则的类型
     *
     * @see RuleType
     */
    RuleType getType();

    /**
     * 在UI中再对规则进行定义的面板
     *
     * @see RuleDefiner
     */
    RuleDefiner createDefiner();

    /**
     * 在UI中再对规则进行定义的面板
     *
     * @see RuleDefiner
     */
    RuleDefiner createDefiner(String serializedString);


}
