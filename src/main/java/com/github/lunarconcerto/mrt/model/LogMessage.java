package com.github.lunarconcerto.mrt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LogMessage {

    public static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    protected String time;

    protected Level level;

    protected String message;

    protected String[] detail;

    public static @NotNull LogMessage debug(String message, String @NotNull ... details) {
        return details.length > 0 ?
                new LogMessage(formatter.format(new Date()), Level.DEBUG, message, details) :
                new LogMessage(formatter.format(new Date()), Level.DEBUG, message, new String[]{message});
    }

    public static @NotNull LogMessage info(String message, String @NotNull ... details) {
        return details.length > 0 ?
                new LogMessage(formatter.format(new Date()), Level.INFO, message, details) :
                new LogMessage(formatter.format(new Date()), Level.INFO, message, new String[]{message});
    }

    public static @NotNull LogMessage warn(String message, String @NotNull ... details) {
        return details.length > 0 ?
                new LogMessage(formatter.format(new Date()), Level.WARN, message, details) :
                new LogMessage(formatter.format(new Date()), Level.WARN, message, new String[]{message});
    }

    public static @NotNull LogMessage error(String message, String @NotNull ... details) {
        return details.length > 0 ?
                new LogMessage(formatter.format(new Date()), Level.ERROR, message, details) :
                new LogMessage(formatter.format(new Date()), Level.ERROR, message, new String[]{message});
    }

    public String getDetail() {
        StringBuilder builder = new StringBuilder();
        for (String s : detail) {
            builder.append(s).append("\n");
        }
        return builder.toString();
    }

    public enum Level {

        DEBUG("调试"),

        INFO("提示"),

        WARN("警告"),

        ERROR("错误");

        final String name;

        Level(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
