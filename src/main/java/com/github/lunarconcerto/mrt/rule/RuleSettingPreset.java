package com.github.lunarconcerto.mrt.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuleSettingPreset {

    protected String presetName ;

    protected List<RuleSettingInfo> infos ;

    public static @NotNull RuleSettingPreset createNewPreset(String presetName, @NotNull List<RuleDefiner> definers){
        RuleSettingPreset preset = new RuleSettingPreset();
        preset.setPresetName(presetName);

        for (RuleDefiner definer : definers) {
            Rule rule = definer.getParentRule();
            if (rule!=null){
                preset.add(RuleSettingInfo.createNewInfo(definer.getParentRule().getClass().getName(),
                        definer.serialize(),
                        definer.getIndex())
                );
            }else {
                preset.add(emptyRuleSettingInfo());
            }
        }

        return preset ;
    }

    public void add(RuleSettingInfo info){
        if (infos == null){
            infos = new ArrayList<>();
        }

        infos.add(info);
    }

    @Contract(" -> new")
    public static @NotNull RuleSettingInfo emptyRuleSettingInfo(){
        return new RuleSettingInfo("empty" , "empty", -1);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuleSettingInfo {

        protected String ruleClassName ;

        protected String serializeData ;

        protected int index ;

        @Contract("_, _, _ -> new")
        public static @NotNull RuleSettingInfo createNewInfo(String ruleClassName, String serializeData, int index){
            return new RuleSettingInfo(ruleClassName, serializeData, index);
        }

        @Contract(" -> new")
        public static @NotNull RuleSettingInfo createNewInfo(){
            return new RuleSettingInfo();
        }


    }

}
