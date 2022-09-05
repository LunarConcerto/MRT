package com.github.lunarconcerto.magicalrenametool.field;

import com.github.lunarconcerto.magicalrenametool.core.MRTApp;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class NameFieldManager {

    static int containerPaneHeight = 40 ;

    protected AnchorPane nameFieldPane ;

    protected List<NameFieldPaneContainer> containerList ;

    protected static NameFieldManager instance;

    public static NameFieldManager getInstance() {
        if (instance == null){
            instance = new NameFieldManager();
        }

        return instance;
    }

    public static void setInstance(NameFieldManager instance) {
        NameFieldManager.instance = instance;
    }

    protected NameFieldManager() {
        this.nameFieldPane = new AnchorPane();
        MRTApp.controller.getName_field_pane().setContent(nameFieldPane);
        this.containerList = new ArrayList<>();
    }

    public void addPane(){
        NameFieldPaneContainer container = new NameFieldPaneContainer(this.nameFieldPane.getChildren().size());
        this.containerList.add(container);
        nameFieldPane.getChildren().add(container);
    }

    public void deletePane(int index){
        this.containerList.remove(index);
        this.nameFieldPane.getChildren().remove(index);
    }

    public static class NameFieldPaneContainer extends AnchorPane {

        int index ;

        public NameFieldPaneContainer(int index) {
            this.index = index;

            this.setWidth(500);
            this.setHeight(containerPaneHeight);
            this.setLayoutY(index * containerPaneHeight);

            Button xButton = new Button("x");
            xButton.setLayoutY(5);
            xButton.setLayoutX(5);
            xButton.setOnAction(event -> {
                NameFieldManager.instance.deletePane(this.index);
            });

            this.getChildren().add(xButton);
            this.getChildren().add(createSeparator());
        }

        Separator createSeparator(){
            Separator separator = new Separator();
            separator.setHalignment(HPos.CENTER);
            separator.setOrientation(Orientation.HORIZONTAL);
            separator.setLayoutY(containerPaneHeight - 10);
            separator.setLayoutX(5);
            separator.setPrefHeight(10);
            separator.setPrefWidth(180);
            return separator;
        }

        public int getIndex() {
            return index;
        }

        public NameFieldPaneContainer setIndex(int index) {
            this.index = index;
            return this;
        }
    }

}
