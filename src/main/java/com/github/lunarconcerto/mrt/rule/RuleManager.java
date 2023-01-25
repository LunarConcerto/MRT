package com.github.lunarconcerto.mrt.rule;

import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.exc.MRTRuleException;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RuleManager {

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
        try (ScanResult scanResult =
                     new ClassGraph().enableClassInfo()
                             .acceptPackages("com.github.lunarconcerto.mrt.rule.impl")
                             .scan()
        ) {
            List<Class<?>> ruleImplList = scanResult.getClassesImplementing(Rule.class.getName())
                    .stream()
                    .map(ClassInfo::loadClass)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (!ruleImplList.isEmpty()) {
                instantiationRules(ruleImplList);
            }
        }
    }

    void instantiationRules(@NotNull List<Class<?>> rules) {
        for (Class<?> ruleClass : rules) {
            try {
                Rule rule = (Rule) ruleClass.getConstructor().newInstance();
                this.ruleList.add(rule);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new MRTRuleException(MRTRuleException.ErrorType.NO_ACCESSIBLE_CONTAINER, ruleClass, "无法实例化");
            }
        }
    }

    void initRules() {
        ruleList.forEach(rule -> rule.init(ConfigurationManager.getManager().getConfiguration()));
    }

    public List<Rule> getAllRules() {
        return ruleList;
    }

}
