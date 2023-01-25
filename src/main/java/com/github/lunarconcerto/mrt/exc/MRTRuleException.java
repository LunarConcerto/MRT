package com.github.lunarconcerto.mrt.exc;

import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.util.Logger;
import org.jetbrains.annotations.NotNull;

public class MRTRuleException extends MRTException {

    static final String ERROR_MESSAGE = "规则定义异常";

    public MRTRuleException(@NotNull ErrorType type, @NotNull Rule rule, String message) {
        String errorMessage = "%s \n  类 %s 上的错误,\n  由于 %s,%s"
                .formatted(ERROR_MESSAGE, rule.getClass().getName(), type.getIllustrate(), message);

        Logger.info(ERROR_MESSAGE, errorMessage);
    }

    public MRTRuleException(@NotNull ErrorType type, @NotNull Class<?> rule, String message) {
        String errorMessage = "%s \n  类 %s 上的错误,\n  由于 %s,%s"
                .formatted(ERROR_MESSAGE, rule.getName(), type.getIllustrate(), message);

        Logger.info(ERROR_MESSAGE, errorMessage);
    }

    public MRTRuleException(@NotNull ErrorType type, String message) {
        String errorMessage = "%s \n,\n  由于 %s,%s"
                .formatted(ERROR_MESSAGE, type.getIllustrate(), message);

        Logger.info(ERROR_MESSAGE, errorMessage);
    }


    public enum ErrorType {

        VER_UNDEFINED("未定义变量"),

        NO_ACCESSIBLE_CONTAINER("无可用构造函数"),

        CLASS_NOT_FOUND("找不到类");

        final String illustrate;

        ErrorType(String illustrate) {
            this.illustrate = illustrate;
        }

        public String getIllustrate() {
            return illustrate;
        }
    }

}
