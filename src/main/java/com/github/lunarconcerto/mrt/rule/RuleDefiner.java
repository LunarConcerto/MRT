package com.github.lunarconcerto.mrt.rule;

import javafx.scene.layout.AnchorPane;

import java.io.Serializable;

public abstract class RuleDefiner extends AnchorPane implements Serializable {

    int index ;

    abstract NameEditor createNameEditor();

    abstract String serialize();

    public int getIndex() {
        return index;
    }

    public RuleDefiner setIndex(int index) {
        this.index = index;
        return this;
    }
}
