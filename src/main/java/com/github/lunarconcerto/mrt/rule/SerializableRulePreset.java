package com.github.lunarconcerto.mrt.rule;

import com.github.lunarconcerto.mrt.component.PresetLoader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 对 {@link RuleDefiner} 的可序列化封装类。
 * 该类的对象保存一整套的 {@link RuleDefiner} 的信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SerializableRulePreset implements Serializable {

    protected String presetName ;

    protected List<RuleSettingInfo> infos ;

    /**
     * 创建一个新的预设类
     *
     * @param presetName 该预设的名字，预设名会反映在载入预设时的选择界面中。
     * @param definers 一套 {@link RuleDefiner},
     *                 <p>
     *                 可无序以及无视 {@link RuleType} ，
     *                 因为这些设置最后将由 {@link RuleDefiner} 中的记录给出。
     * @return
     */
    public static @NotNull SerializableRulePreset createNewPreset(String presetName, @NotNull List<RuleDefiner> definers){
        SerializableRulePreset preset = new SerializableRulePreset();
        preset.setPresetName(presetName);

        for (RuleDefiner definer : definers) {
            preset.add(definer.getParentRule() != null ?
                    /* 非空即创建该rule的可序列化封装类 */
                    RuleSettingInfo.createNewInfo(definer.getParentRule().getClass().getName(), definer.serialize(), definer.getIndex()) :
                    /* 若为空rule，创建一个空的封装类 */
                    emptyRuleSettingInfo()
            );
        }

        return preset ;
    }

    /**
     * 为该预设添加一个 {@link RuleDefiner} 的封装类实例。
     * @param info 添加的目标 {@link RuleDefiner}
     */
    public void add(RuleSettingInfo info){
        if (infos == null){
            infos = new ArrayList<>();
        }

        infos.add(info);
    }

    /**
     * 转换成一个 {@link RuleDefinerPreset}，
     * 其中包含已经反序列化的 {@link RuleDefiner} 对象。
     * @see RuleDefinerPreset
     * @return 该对象对应的 {@link RuleDefinerPreset}
     */
    public RuleDefinerPreset toRuleDefinerPreset(){
        return new PresetLoader().loadRuleDefinersFromPreset(this);
    }

    /**
     * 返回该预设的名字
     * @return {@link SerializableRulePreset#presetName}
     */
    @Override
    public String toString() {
        return presetName ;
    }

    /**
     * 返回一个空 {@link RuleSettingInfo} ,用于占位以防空指针等。
     * @return 一个没什么用的空 {@link RuleSettingInfo}
     */
    @Contract(" -> new")
    public static @NotNull RuleSettingInfo emptyRuleSettingInfo(){
        return new RuleSettingInfo("empty" , "empty", -1);
    }

    /**
     * 对 {@link RuleDefiner} 的可序列化封装类。
     * 该类的对象会保存单个 {@link RuleDefiner} 的信息。
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuleSettingInfo {

        protected String ruleClassName ;

        protected String serializeData ;

        protected int index ;

        /**
         * 对该类的new封装
         * @param ruleClassName 要存储的 {@link RuleDefiner} 的对应 {@link Rule} 实现的全类名，
         *                      反序列化时使用。
         * @param serializeData 要存储的 {@link RuleDefiner} 的对应的序列化值，
         *                      该值一般调用 {@link RuleDefiner#serialize()} 方法获取，
         *                      反序列化时，调用对应 Rule 的{@link Rule#createDefiner(String)} 方法加载。
         * @param index 该 {@link RuleDefiner} 在列表中的序号。
         * @return 一个 new {@link RuleDefiner}
         */
        @Contract("_, _, _ -> new")
        public static @NotNull RuleSettingInfo createNewInfo(String ruleClassName, String serializeData, int index){
            return new RuleSettingInfo(ruleClassName, serializeData, index);
        }

        /**
         * 对该类的new封装
         * @return 一个 new {@link RuleDefiner}
         */
        @Contract(" -> new")
        public static @NotNull RuleSettingInfo createNewInfo(){
            return new RuleSettingInfo();
        }

    }

}
