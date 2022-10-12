package com.github.lunarconcerto.mrt.gui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;
import org.jetbrains.annotations.NotNull;

public class PopOvers {

    private PopOvers() {}

    public static void showPopOver(Node owner, String @NotNull ... text){
        VBox box = new VBox(createLabels(text));
        new PopOver(box).show(owner);
    }

    private static Label @NotNull [] createLabels(String @NotNull ... text){
        Label[] labels = new Label[text.length + 1] ;

        labels[0] = new Label(" ");
        for (int i = 0, textLength = text.length; i < textLength; i++) {
            labels[i+1] = new Label("   " + text[i] + "    ");
        }

        return labels ;
    }

}
