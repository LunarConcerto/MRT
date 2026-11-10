package com.github.lunarconcerto.mrt.rule;

import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.exc.MRTRuleException;
import com.github.lunarconcerto.mrt.rule.impl.DlsiteOnseiElement;
import com.github.lunarconcerto.mrt.rule.impl.PureElement;
import com.github.lunarconcerto.mrt.rule.impl.PureRemove;
import com.github.lunarconcerto.mrt.rule.impl.PureReplace;
import com.github.lunarconcerto.mrt.rule.impl.PureText;
import com.github.lunarconcerto.mrt.rule.impl.ReplaceExtension;
import com.github.lunarconcerto.mrt.rule.impl.TextDecoder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RuleManager {

    // 已保存的预设使用规则的全限定类名作为标识。
    private static final Map<String, Supplier<? extends Rule>> BUILT_IN_RULES = new LinkedHashMap<>();

    static {
        register(DlsiteOnseiElement.class, DlsiteOnseiElement::new);
        register(PureElement.class, PureElement::new);
        register(PureRemove.class, PureRemove::new);
        register(PureReplace.class, PureReplace::new);
        register(PureText.class, PureText::new);
        register(ReplaceExtension.class, ReplaceExtension::new);
        register(TextDecoder.class, TextDecoder::new);
    }

    private static RuleManager instance;
    protected List<Rule> ruleList;

    public RuleManager() {
        loadRules();
    }

    public static RuleManager getInstance() {
        if (instance == null) {
            instance = new RuleManager();
        }

        return instance;
    }

    void loadRules() {
        ruleList = new ArrayList<>();

        loadInternalRules();

        initRules();
    }

    void loadInternalRules() {
        BUILT_IN_RULES.values().forEach(factory -> ruleList.add(factory.get()));
    }

    private static <T extends Rule> void register(Class<T> type, Supplier<T> factory) {
        BUILT_IN_RULES.put(type.getName(), factory);
    }

    public static Rule createRule(String className) {
        Supplier<? extends Rule> factory = BUILT_IN_RULES.get(className);
        if (factory == null) {
            throw new MRTRuleException(MRTRuleException.ErrorType.CLASS_NOT_FOUND,
                    "未注册的命名规则 " + className);
        }
        return factory.get();
    }

    void initRules() {
        ruleList.forEach(rule -> rule.init(ConfigurationManager.getManager().getConfiguration()));
    }

    public List<Rule> getAllRules() {
        return ruleList;
    }

}
