package com.github.lunarconcerto.mrt.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

import static com.github.lunarconcerto.mrt.MRTApplication.mainController;

@Slf4j(topic = "log")
public class Logger {

    public static void debug(String text, String... details) {
        if (mainController != null) {
            mainController.info(text, details);
        }

        log.debug(text);
        if (details.length > 0) log.info(Arrays.toString(details));
    }

    public static void info(String text, String... details) {
        if (mainController != null) {
            mainController.info(text, details);
        }

        log.info(text);
        if (details.length > 0) log.info(Arrays.toString(details));
    }

    public static void warn(String text, String... details) {
        if (mainController != null) {
            mainController.warn(text, details);
        }

        log.warn(text);
        if (details.length > 0) log.warn(Arrays.toString(details));
    }

    public static void error(String text, String... details) {
        if (mainController != null) {
            mainController.error(text, details);
        }

        log.error(text);
        if (details.length > 0) log.error(Arrays.toString(details));
    }

}
