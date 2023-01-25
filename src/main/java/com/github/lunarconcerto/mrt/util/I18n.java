package com.github.lunarconcerto.mrt.util;

import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class I18n {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("i18n/i18n");

    public static @NotNull String get(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }

}
