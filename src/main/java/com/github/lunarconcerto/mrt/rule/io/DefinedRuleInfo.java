package com.github.lunarconcerto.mrt.rule.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.exc.MRTRuleException;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.util.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;


/**
 * 对 {@link RuleDefiningPane} 的可序列化封装类。
 * 该类的对象会保存单个 {@link RuleDefiningPane} 的信息。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefinedRuleInfo {


    protected String ruleClassName;

    protected String serializeData;

    protected int index;

    public static RuleDefiningPane deserialize(String serializeData) {
        try {
            DefinedRuleInfo info = Json.MAPPER.readValue(serializeData, DefinedRuleInfo.class);
            return info.toRuleDefiner();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对该类的new封装
     *
     * @param ruleClassName 要存储的 {@link RuleDefiningPane} 的对应 {@link Rule} 实现的全类名，
     *                      反序列化时使用。
     * @param serializeData 要存储的 {@link RuleDefiningPane} 的对应的序列化值，
     *                      该值一般调用 {@link RuleDefiningPane#serialize()} 方法获取，
     *                      反序列化时，调用对应 Rule 的{@link Rule#createDefiningPane(String)} 方法加载。
     * @param index         该 {@link RuleDefiningPane} 在列表中的序号。
     * @return 一个 new {@link DefinedRuleInfo}
     */
    @Contract("_, _, _ -> new")
    public static @NotNull DefinedRuleInfo create(String ruleClassName, String serializeData, int index) {
        return new DefinedRuleInfo(ruleClassName, serializeData, index);
    }

    /**
     * 对该类的new封装
     *
     * @param definer 目标 {@link RuleDefiningPane} 对象
     * @return 一个 new {@link DefinedRuleInfo}
     */
    public static @NotNull DefinedRuleInfo create(@NotNull RuleDefiningPane definer) {
        return create(definer.getParentRule().getClass().getName(), definer.serialize(), definer.getIndex());
    }

    /**
     * 对该类的new封装
     *
     * @return 一个 new {@link DefinedRuleInfo}
     */
    @Contract(" -> new")
    public static @NotNull DefinedRuleInfo create() {
        return new DefinedRuleInfo();
    }

    public String serialize() {
        try {
            return Json.MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public RuleDefiningPane toRuleDefiner() {
        try {
            Class<?> ruleClass = Class.forName(this.getRuleClassName());
            Rule rule = (Rule) ruleClass.getConstructor().newInstance();

            RuleDefiningPane definer;
            String data = this.getSerializeData();

            definer = data != null && !data.isEmpty() ?
                    /* 序列化值非空的情况下调用反序列化的创建方法 */
                    rule.createDefiningPane(data) :
                    rule.createDefiningPane();

            definer.setIndex(this.getIndex());

            return definer;
        } catch (ClassNotFoundException e) {
            throw new MRTRuleException(MRTRuleException.ErrorType.CLASS_NOT_FOUND,
                    "无法找到类" + this.getRuleClassName());
        } catch (NoSuchMethodException e) {
            throw new MRTRuleException(MRTRuleException.ErrorType.NO_ACCESSIBLE_CONTAINER,
                    "无可用构造函数" + this.getRuleClassName());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException ignored) {
            throw new MRTRuleException(MRTRuleException.ErrorType.NO_ACCESSIBLE_CONTAINER,
                    "其他错误" + this.getRuleClassName());
        }
    }

}
