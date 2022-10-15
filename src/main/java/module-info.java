module com.github.lunarconcerto.mrt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome;

    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires org.jsoup;
    requires log4j;
    requires lombok;
    requires io.github.classgraph;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires com.jfoenix;

    exports com.github.lunarconcerto.mrt;
    exports com.github.lunarconcerto.mrt.util;
    exports com.github.lunarconcerto.mrt.config;
    exports com.github.lunarconcerto.mrt.component;
    exports com.github.lunarconcerto.mrt.rule;
    exports com.github.lunarconcerto.mrt.gui;

    opens com.github.lunarconcerto.mrt to javafx.fxml;
    opens com.github.lunarconcerto.mrt.config to javafx.fxml;
    opens com.github.lunarconcerto.mrt.component to javafx.fxml;
    opens com.github.lunarconcerto.mrt.gui to javafx.fxml;
}