package com.github.lunarconcerto.magicalrenametool.field;

import com.github.lunarconcerto.magicalrenametool.core.MRTApp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.Collections;
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

    public void deletePane(NameFieldPaneContainer container){
        container.setVisible(false);

        this.containerList.remove(container);
        this.nameFieldPane.getChildren().remove(container);
    }

    public void upMovePane(NameFieldPaneContainer container){
        int i = container.getIndex();
        int j = i - 1 ;
        if (j != -1){
            Collections.swap(this.containerList, i, j);

            container.resetLocation(j);
            ((NameFieldPaneContainer) this.nameFieldPane.getChildren().get(j)).resetLocation(i);
        }


    }

    public static class NameFieldPaneContainer extends AnchorPane {

        int index ;

//        Label name ;

        public NameFieldPaneContainer(int index) {
            this.index = index;

//            name = new Label(""+this.index);
//            name.setLayoutX(50);
//            this.getChildren().add(name);
            init();

            this.getChildren().add(createControlButton());
            this.getChildren().add(createSeparator());
        }

        public void init(){
            this.setWidth(500);
            this.setHeight(containerPaneHeight);
            this.setLayoutY(index * containerPaneHeight);
        }

        public void resetLocation(int index){
            this.index = index;

            init();
        }

        SplitMenuButton createControlButton(){
            SplitMenuButton controlButton = new SplitMenuButton();
            controlButton.setMinSize(24 , 20);
            controlButton.setPrefSize(0, 0);
            controlButton.setMaxSize(0 , 0);
            controlButton.setLayoutY(6);
            controlButton.setLayoutX(5);
            controlButton.getItems().addAll(
                    createMenuItem("删除" , event -> NameFieldManager.instance.deletePane(this)),
                    createMenuItem("上移", event -> {NameFieldManager.instance.upMovePane(this);}),
                    createMenuItem("下移", event -> {})
                    );
            return controlButton;
        }

        MenuItem createMenuItem(String text , EventHandler<ActionEvent> handler){
            MenuItem item = new MenuItem(text);
            item.setOnAction(handler);
            return item ;
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
