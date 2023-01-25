package com.github.lunarconcerto.mrt.util;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import org.controlsfx.control.PopOver;
import org.jetbrains.annotations.NotNull;

public class PopOvers {

    private PopOvers() {
    }

    public static void showPopOver(Node owner, String @NotNull ... text) {
        TextFlow flow = new TextFlow();
        StackPane pane = new StackPane(flow);
        StackPane.setMargin(flow, new Insets(15, 15, 15, 15));
        flow.setLineSpacing(10);
        flow.getChildren().addAll(Texts.texts(text));

        PopOver popOver = new PopOver(pane);
        popOver.setAutoHide(true);
        popOver.setTitle("提示");
        popOver.show(owner);
    }

}
