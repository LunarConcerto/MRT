package com.github.lunarconcerto.magicalrenametool.exc;

import com.github.lunarconcerto.magicalrenametool.core.MRTApp;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.io.PrintWriter;

public class MRTException extends RuntimeException {

    public MRTException() {

        printStackTrace();
    }

    public MRTException(String message) {
        super(message);

        printStackTrace();
    }

    public MRTException(String message, Throwable cause) {
        super(message, cause);

        printStackTrace();
    }

    public MRTException(Throwable cause) {
        super(cause);

        printStackTrace();
    }

    public MRTException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

        printStackTrace();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();

        MRTApp.printToUIConsole(buildErrorText(this));
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);

        MRTApp.printToUIConsole(buildErrorText(this));
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);

        MRTApp.printToUIConsole(buildErrorText(this));
    }

    public static @NotNull String buildErrorText(@NotNull Throwable e){
        StringBuilder builder = new StringBuilder();

        builder.append("程序抛出了一个异常.").append("\n")
                .append("异常信息: ").append(e.getMessage()).append("\n");

        StackTraceElement[] stackTrace = e.getStackTrace();

        for (StackTraceElement traceElement : stackTrace) {
            builder.append("  在>").append(traceElement).append("\n");
        }
        builder.append("您可以百度搜索问题原因, 或者在github上提交issue.");

        return builder.toString() ;
    }



}
