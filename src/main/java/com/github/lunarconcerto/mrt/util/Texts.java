package com.github.lunarconcerto.mrt.util;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Texts {

    public static final FontIcon ALWAYS_ON_TOP_OFF = new FontIcon("mdi2p-pin-off");
    public static final FontIcon ALWAYS_ON_TOP_ON = new FontIcon("mdi2p-pin");
    public static final FontIcon HIDE_LOGGER_OFF = new FontIcon("mdi2m-message-bulleted-off");
    public static final FontIcon HIDE_LOGGER_ON = new FontIcon("mdi2m-message-bulleted");
    public static final FontIcon RULE_ADD = new FontIcon("mdi2t-toy-brick-plus-outline");
    public static final FontIcon RULE_REMOVE = new FontIcon("mdi2t-toy-brick-minus");
    public static final FontIcon RULE_COPY = new FontIcon("mdi2t-toy-brick-plus");
    public static final FontIcon RULE_CLEAR = new FontIcon("mdi2t-toy-brick-remove");
    public static final FontIcon FILE_SELECT = new FontIcon("mdi2s-select");
    public static final FontIcon FILE_SELECT_ALL = new FontIcon("mdi2s-select-all");
    public static final FontIcon FILE_SELECT_INVERSE = new FontIcon("mdi2s-select-inverse");
    public static final FontIcon FILE_REFRESH = new FontIcon("mdi2f-folder-refresh-outline");

    private Texts() {
    }

    public static Font fontSmileySens(int size) {
        return Font.loadFont(FileUtil.getResourceAsStream("font/SmileySans-Oblique.ttf"), size);
    }

    public static Text[] texts(Text... text) {
        return text;
    }

    public static @NotNull List<Text> texts(String @NotNull ... strings) {
        return Arrays.stream(strings)
                .map(Texts::textWithTabNewLine)
                .collect(Collectors.toList());
    }

    @Contract("_ -> new")
    public static @NotNull Text textWithTabNewLine(String str) {
        return new Text("\t" + str + "\n");
    }

    public static @NotNull Text textWithTabNewLine(String string, Color color) {
        Text text = textWithTabNewLine(string);
        text.setFill(color);
        return text;
    }

    public static @NotNull Text textWithTabNewLine(String string, Font font) {
        Text text = textWithTabNewLine(string);
        text.setFont(font);
        return text;
    }

    public static @NotNull Text textWithTabNewLine(String string, Color color, Font font) {
        Text text = textWithTabNewLine(string);
        text.setFill(color);
        text.setFont(font);
        return text;
    }

    @Contract("_ -> new")
    public static @NotNull Text textWithTab(String str) {
        return new Text("\t" + str);
    }

    public static @NotNull Text textWithTab(String string, Color color) {
        Text text = textWithTab(string);
        text.setFill(color);
        return text;
    }

    public static @NotNull Text textWithTab(String string, Font font) {
        Text text = textWithTab(string);
        text.setFont(font);
        return text;
    }

    public static @NotNull Text textWithTab(String string, Color color, Font font) {
        Text text = textWithTab(string);
        text.setFill(color);
        text.setFont(font);
        return text;
    }

    @Contract("_ -> new")
    public static @NotNull Text textWithNewLine(String str) {
        return new Text(str + "\n");
    }

    public static @NotNull Text textWithNewLine(String string, Color color) {
        Text text = textWithNewLine(string);
        text.setFill(color);
        return text;
    }

    public static @NotNull Text textWithNewLine(String string, Font font) {
        Text text = textWithNewLine(string);
        text.setFont(font);
        return text;
    }

    public static @NotNull Text textWithNewLine(String string, Color color, Font font) {
        Text text = textWithNewLine(string);
        text.setFill(color);
        text.setFont(font);
        return text;
    }

    @Contract("_ -> new")
    public static @NotNull Text text(String str) {
        return new Text(str);
    }

    public static @NotNull Text text(String string, Color color) {
        Text text = text(string);
        text.setFill(color);
        return text;
    }

    public static @NotNull Text text(String string, Font font) {
        Text text = text(string);
        text.setFont(font);
        return text;
    }

    public static @NotNull Text text(String string, Color color, Font font) {
        Text text = text(string);
        text.setFill(color);
        text.setFont(font);
        return text;
    }
}
