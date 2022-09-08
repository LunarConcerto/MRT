package com.github.lunarconcerto.magicalrenametool.field;

import javafx.scene.layout.AnchorPane;

import java.util.Properties;

public interface NameField {

    String getName();

    AnchorPane getNameFieldPane();

    AnchorPane getExtraSettingsPane();

    void setIndex(int index);

    int getIndex();

    void save(Properties properties);

    void load(Properties properties);

    void run(StringBuilder builder);

}
