open module com.github.lunarconcerto.mrt {
    requires static org.jetbrains.annotations;
    requires static lombok;
    requires static jsr305;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.slf4j;

    requires animatefx;
    requires org.jsoup;

    requires io.github.classgraph;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    requires java.desktop;
    requires com.dlsc.preferencesfx;
    requires MaterialFX;
    requires com.dustinredmond.fxtrayicon;


    exports com.github.lunarconcerto.mrt;
    exports com.github.lunarconcerto.mrt.util;
    exports com.github.lunarconcerto.mrt.config;
    exports com.github.lunarconcerto.mrt.task;
    exports com.github.lunarconcerto.mrt.rule;
    exports com.github.lunarconcerto.mrt.rule.io;
    exports com.github.lunarconcerto.mrt.controller;
    exports com.github.lunarconcerto.mrt.model;
    exports com.github.lunarconcerto.mrt.control;

}