package com.github.lunarconcerto.mrt.gui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.jetbrains.annotations.NotNull;

public class PopOvers {

    private PopOvers() {}

    public static void showPopOver(Node owner, String @NotNull ... text){
        Label[] labels = new Label[text.length] ;
        for (int i = 0, textLength = text.length; i < textLength; i++) {
            labels[i] = new Label(text[i]);
        }

        VBox box = new VBox(labels);
        new PopOver(box).show(owner);
    }

}
