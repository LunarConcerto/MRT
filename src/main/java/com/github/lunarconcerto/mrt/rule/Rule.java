package com.github.lunarconcerto.mrt.rule;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.controller.RuleSelectorController;
import com.github.lunarconcerto.mrt.util.Texts;
import javafx.scene.text.Text;

import static com.github.lunarconcerto.mrt.util.Texts.texts;

public interface Rule {

    /**
     * 在 {@link Rule} 被实例化后调用,
     * <p>
     * 在该方法中，可以为该 {@link Rule} 保存配置，
     * 以及添加配置词条到配置面板
     *
     * @param configuration 当前程序已加载的配置类.
     * @see Configuration
     * @see com.github.lunarconcerto.mrt.config.ConfigurationManager
     */
    void init(Configuration configuration);

    /**
     * 显示在 {@link RuleSelectorController}
     * 面板上的名字。
     *
     * @see RuleSelectorController
     */
    String getName();


    /**
     * 显示在 {@link RuleSelectorController}
     * 中，对该规则的描述。
     *
     * @see RuleSelectorController
     */
    default Text[] getDescription() {
        return texts(Texts.textWithTabNewLine("暂无描述."));
    }

    /**
     * 在主面板中再对规则进行细节自定义的面板
     *
     * @see RuleDefiningPane
     */
    RuleDefiningPane createDefiningPane();

    /**
     * 在主面板中再对规则进行细节自定义的面板
     * <p>
     * 该方法输入一个从 {@link RuleDefiningPane#serialize() } 方法
     * 获取的序列化数据,
     * 故该方法的实现, 需要返回一个反序列化的 {@link RuleDefiningPane}.
     *
     * @see RuleDefiningPane
     */
    RuleDefiningPane createDefiningPane(String serializedString);


}
