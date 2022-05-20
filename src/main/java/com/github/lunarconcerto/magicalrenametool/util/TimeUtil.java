package com.github.lunarconcerto.magicalrenametool.util;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    private TimeUtil() {
    }

    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 获取已格式化后的当前时间
     * @return String
     */
    public static @NotNull String getFormattedNowTime(){
        Date date = new Date();
        return formatter.format(date);
    }

}
