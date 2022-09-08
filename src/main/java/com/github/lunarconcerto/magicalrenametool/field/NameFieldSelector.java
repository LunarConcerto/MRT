package com.github.lunarconcerto.magicalrenametool.field;

import javafx.scene.layout.AnchorPane;

import java.util.Properties;

public class NameFieldSelector extends AbstractNameField {

    @Override
    public AnchorPane getNameFieldPane() {
        return new AnchorPane();
    }

    @Override
    public AnchorPane getExtraSettingsPane() {
        return new AnchorPane();
    }

    @Override
    public void save(Properties properties) {}

    @Override
    public void load(Properties properties) {}

    @Override
    public void run(StringBuilder builder) {}

    static class SelectorPane extends AnchorPane {

        public SelectorPane() {

        }


    }

}
