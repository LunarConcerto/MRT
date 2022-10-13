package com.github.lunarconcerto.mrt.rule;

import com.github.lunarconcerto.mrt.config.Configuration;

public interface Rule {

    /**
     * 在 {@link Rule} 被实例化后调用,
     * <p>
     * 在该方法中，可以为该 {@link Rule} 保存配置，
     * 以及添加配置词条到配置面板
     * @param configuration 当前程序已加载的配置类.
     * @see Configuration
     * @see com.github.lunarconcerto.mrt.config.ConfigurationManager
     */
    void init(Configuration configuration);

    /**
     * 显示在 {@link com.github.lunarconcerto.mrt.gui.MRTRuleSelectorPaneController}
     * 面板上的名字。
     *
     * @see com.github.lunarconcerto.mrt.gui.MRTRuleSelectorPaneController
     */
    String getName();


    /**
     * 显示在 {@link com.github.lunarconcerto.mrt.gui.MRTRuleSelectorPaneController}
     * 面板上的，对该规则的描述。
     *
     * @see com.github.lunarconcerto.mrt.gui.MRTRuleSelectorPaneController
     */
    default String getDescription(){
        return "暂无描述." ;
    }

    /**
     * 在主面板中再对规则进行细节自定义的面板
     *
     * @see RuleDefiner
     * @see com.github.lunarconcerto.mrt.gui.MRTController#ruleDefinerShower
     * @see com.github.lunarconcerto.mrt.gui.MRTController#ruleReplaceSetter
     */
    RuleDefiner createDefiner();

    /**
     * 在主面板中再对规则进行细节自定义的面板
     * <p>
     * 该方法输入一个从 {@link RuleDefiner#serialize() } 方法
     * 获取的序列化数据,
     * 故该方法的实现, 需要返回一个反序列化的 {@link RuleDefiner}.
     * @see RuleDefiner
     * @see com.github.lunarconcerto.mrt.gui.MRTController#ruleDefinerShower
     * @see com.github.lunarconcerto.mrt.gui.MRTController#ruleReplaceSetter
     */
    RuleDefiner createDefiner(String serializedString);


}
