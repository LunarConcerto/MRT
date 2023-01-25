open module com.github.lunarconcerto.mrt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires org.jsoup;
    requires lombok;
    requires io.github.classgraph;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires com.dlsc.preferencesfx;
    requires MaterialFX;
    requires com.dustinredmond.fxtrayicon;
    requires AnimateFX;

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