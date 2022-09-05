module com.github.lunarconcerto.magicalrenametool {
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

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports com.github.lunarconcerto.magicalrenametool;
    exports com.github.lunarconcerto.magicalrenametool.util;
    exports com.github.lunarconcerto.magicalrenametool.config;
    exports com.github.lunarconcerto.magicalrenametool.component;
    exports com.github.lunarconcerto.magicalrenametool.rule;
    exports com.github.lunarconcerto.magicalrenametool.core;
    exports com.github.lunarconcerto.magicalrenametool.rule.impl.dlsite.onsei;

    opens com.github.lunarconcerto.magicalrenametool to javafx.fxml;
    opens com.github.lunarconcerto.magicalrenametool.config to javafx.fxml;
    opens com.github.lunarconcerto.magicalrenametool.component to javafx.fxml;
    opens com.github.lunarconcerto.magicalrenametool.core to javafx.fxml;
}