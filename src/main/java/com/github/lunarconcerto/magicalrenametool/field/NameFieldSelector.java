package com.github.lunarconcerto.magicalrenametool.field;

import javafx.scene.layout.AnchorPane;

import java.util.Properties;

public class NameFieldSelector extends AbstractNameField {

    @Override
    public AnchorPane getNameFieldPane() {
        return null;
    }

    @Override
    public AnchorPane getExtraSettingsPane() {
        return null;
    }

    @Override
    public void save(Properties properties) {}

    @Override
    public void load(Properties properties) {}

    @Override
    public void run(StringBuilder builder) {}

}
