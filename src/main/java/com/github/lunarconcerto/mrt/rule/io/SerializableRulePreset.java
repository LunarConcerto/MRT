package com.github.lunarconcerto.mrt.rule.io;

import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 对 {@link RuleDefiningPane} 的可序列化封装类。
 * 该类的对象保存一整套的 {@link RuleDefiningPane} 的信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerializableRulePreset implements Serializable {

    protected String presetName;

    protected List<DefinedRuleInfo> infos;

    /**
     * 创建一个新的预设类
     *
     * @param presetName 该预设的名字，预设名会反映在载入预设时的选择界面中。
     * @param definers   一套 {@link RuleDefiningPane},
     *                   <p>
     *                   因为这些设置最后将由 {@link RuleDefiningPane} 中的记录给出。
     * @return
     */
    public static @NotNull SerializableRulePreset createNewPreset(String presetName, @NotNull List<RuleDefiningPane> definers) {
        SerializableRulePreset preset = new SerializableRulePreset();
        preset.setPresetName(presetName);

        for (RuleDefiningPane definer : definers) {
            preset.add(definer.getParentRule() != null ?
                    /* 非空即创建该rule的可序列化封装类 */
                    definer.toDefinedInfo() :
                    /* 若为空rule，创建一个空的封装类 */
                    emptyRuleSettingInfo()
            );
        }

        return preset;
    }

    /**
     * 返回一个空 {@link DefinedRuleInfo} ,用于占位以防空指针等。
     *
     * @return 一个没什么用的空 {@link DefinedRuleInfo}
     */
    @Contract(" -> new")
    public static @NotNull DefinedRuleInfo emptyRuleSettingInfo() {
        return new DefinedRuleInfo("empty", "empty", -1);
    }

    /**
     * 为该预设添加一个 {@link RuleDefiningPane} 的封装类实例。
     *
     * @param info 添加的目标 {@link RuleDefiningPane}
     */
    public void add(DefinedRuleInfo info) {
        if (infos == null) {
            infos = new ArrayList<>();
        }

        infos.add(info);
    }

    /**
     * 转换成一个 {@link RuleDefinerPreset}，
     * 其中包含已经反序列化的 {@link RuleDefiningPane} 对象。
     *
     * @return 该对象对应的 {@link RuleDefinerPreset}
     * @see RuleDefinerPreset
     */
    public RuleDefinerPreset toRuleDefinerPreset() {
        return new PresetLoader().loadRuleDefinersFromPreset(this);
    }

    /**
     * 返回该预设的名字
     *
     * @return {@link SerializableRulePreset#presetName}
     */
    @Override
    public String toString() {
        return presetName;
    }

}
