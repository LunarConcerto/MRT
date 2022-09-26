package com.github.lunarconcerto.mrt.exc;

import com.github.lunarconcerto.mrt.gui.MRTApp;
import com.github.lunarconcerto.mrt.rule.Rule;

public class MRTRuleException extends MRTException {

    static final String ERROR_MESSAGE = "规则定义异常: ";

    public MRTRuleException(ErrorType type, Rule rule, String message) {
        String errorMessage = "%s \n  类 %s 上的错误,\n  由于 %s,%s".formatted(ERROR_MESSAGE, rule.getClass().getName(), type.getIllustrate(), message);

        MRTApp.printToUiLogger(errorMessage);
    }

    public MRTRuleException(ErrorType type, Class<?> rule, String message) {
        String errorMessage = "%s \n  类 %s 上的错误,\n  由于 %s,%s".formatted(ERROR_MESSAGE, rule.getName(), type.getIllustrate(), message);

        MRTApp.printToUiLogger(errorMessage);
    }


    public enum ErrorType {

        VER_UNDEFINED("未定义变量") ,

        NO_ACCESSIBLE_CONTAINER("无可用构造函数");

        ;

        final String illustrate ;

        ErrorType(String illustrate) {
            this.illustrate = illustrate;
        }

        public String getIllustrate() {
            return illustrate;
        }
    }

}
