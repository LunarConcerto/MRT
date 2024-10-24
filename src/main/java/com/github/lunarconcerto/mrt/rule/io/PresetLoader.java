package com.github.lunarconcerto.mrt.rule.io;

import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.exc.MRTUIException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PresetLoader {

    RuleDefinerPreset preset;

    List<RuleDefiningPane> definerList;

    public PresetLoader() {
        preset = new RuleDefinerPreset();
        definerList = this.preset.getRuleDefiningPaneList();
    }

    public static RuleDefinerPreset load(@NotNull SerializableRulePreset preset) {
        return new PresetLoader().loadRuleDefinersFromPreset(preset);
    }

    public RuleDefinerPreset loadRuleDefinersFromPreset(@NotNull SerializableRulePreset preset) {
        List<DefinedRuleInfo> infos = preset.getInfos();
        if (infos != null && !infos.isEmpty()) {
            for (DefinedRuleInfo info : infos) {
                RuleDefiningPane definer = info.toRuleDefiner();

                int index = definer.getIndex();
                if (index > definerList.size()) {
                    throw new MRTUIException("读取预设时出现错误.");
                }
                definerList.add(index, definer);
            }
        }
        return this.preset;
    }

}
