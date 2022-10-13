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

    protected List<Rule> ruleList ;

    private static RuleManager instance ;

    public static RuleManager getInstance(){
        if (instance==null){
            instance = new RuleManager();
        }

        return instance;
    }

    public RuleManager() {
        loadRules();
    }

    void loadRules(){
        ruleList = new ArrayList<>();

        loadInternalRules();

        initRules();
    }

    void loadInternalRules(){
        String pkg = "com.github.lunarconcerto.mrt.rule.impl" ;
        try (ScanResult scanResult =
                     new ClassGraph()
                             /* 输出Log */
//                             .verbose()
                             .enableClassInfo()
                             .acceptPackages(pkg)
                             .scan()
        ){
            List<Class<?>> ruleImplList = scanResult.getClassesImplementing(Rule.class.getName())
                    .stream()
                    .map(ClassInfo::loadClass)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (!ruleImplList.isEmpty()){
                instantiationRules(ruleImplList);
            }
        }
    }

    void instantiationRules(@NotNull List<Class<?>> rules) {
        for (Class<?> ruleClass : rules) {
            try {
                Rule rule = (Rule) ruleClass.getConstructor().newInstance();
                this.ruleList.add(rule);
            }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
                throw new MRTRuleException(MRTRuleException.ErrorType.NO_ACCESSIBLE_CONTAINER, ruleClass, "无法实例化");
            }
        }
    }

    void initRules(){
        ruleList.forEach(rule -> rule.init(ConfigurationManager.getManager().getConfiguration()));
    }

    public List<Rule> getAllRules() {
        return ruleList;
    }

}
