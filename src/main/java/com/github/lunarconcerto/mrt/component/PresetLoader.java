package com.github.lunarconcerto.mrt.component;

import com.github.lunarconcerto.mrt.exc.MRTRuleException;
import com.github.lunarconcerto.mrt.rule.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class PresetLoader {

    RuleDefinerPreset preset ;

    public PresetLoader() {
        preset = new RuleDefinerPreset();
    }

    public static RuleDefinerPreset load(@NotNull SerializableRulePreset preset){
        return new PresetLoader().loadRuleDefinersFromPreset(preset);
    }

    public RuleDefinerPreset loadRuleDefinersFromPreset(@NotNull SerializableRulePreset preset){
        List<SerializableRulePreset.RuleSettingInfo> infos = preset.getInfos();
        if (infos!=null && !infos.isEmpty()){
            for (SerializableRulePreset.RuleSettingInfo info : infos) {
                try {
                    Class<?> ruleClass = Class.forName(info.getRuleClassName());
                    Rule instance = (Rule) ruleClass.getConstructor().newInstance();

                    loadRuleDefinerFromSettingInfo(info, instance);
                }catch (ClassNotFoundException e){
                    throw new MRTRuleException(MRTRuleException.ErrorType.CLASS_NOT_FOUND, "无法找到类" + info.getRuleClassName());
                }catch (NoSuchMethodException e){
                    throw new MRTRuleException(MRTRuleException.ErrorType.NO_ACCESSIBLE_CONTAINER, "无可用构造函数" + info.getRuleClassName());
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException ignored) {

                }
            }
        }
        return this.preset ;
    }

    void loadRuleDefinerFromSettingInfo(SerializableRulePreset.@NotNull RuleSettingInfo info, Rule rule){
        RuleDefiner definer;
        String data = info.getSerializeData();
        if (data!=null && !data.isEmpty()){
            definer = rule.createDefiner(data);
        }else {
            definer = rule.createDefiner();
        }
        definer.setIndex(info.getIndex());

        addToList(definer, rule.getType());
    }

    void addToList(RuleDefiner definer, @NotNull RuleType type){
        switch (type){
            case FILLING -> preset.getFillingRuleList().add(definer.getIndex(), definer);
            case REPLACE -> preset.getReplaceRuleList().add(definer.getIndex(), definer);
        }
    }

}
