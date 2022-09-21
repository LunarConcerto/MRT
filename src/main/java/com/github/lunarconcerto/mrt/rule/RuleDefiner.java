package com.github.lunarconcerto.mrt.rule;

import javafx.scene.layout.AnchorPane;

import java.io.Serializable;

public abstract class RuleDefiner extends AnchorPane implements Serializable {

    abstract NameEditor createNameEditor();

    abstract String saveToString();

    abstract void loadFromString(String text);

}
