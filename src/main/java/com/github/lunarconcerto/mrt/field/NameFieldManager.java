package com.github.lunarconcerto.mrt.field;

import com.github.lunarconcerto.mrt.core.MRTApp;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NameFieldManager {

    List<Class<NameField>> nameFieldTypes ;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     *                   面板相关变量
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    static int containerPaneHeight = 40 ;

    protected AnchorPane nameFieldPane ;

    protected List<NameFieldPaneContainer> containerList ;

    protected static NameFieldManager instance;

    public static NameFieldManager getInstance() {
        if (instance == null) instance = new NameFieldManager();
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

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     *                    NameField类管理
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void registerNameField(Class<NameField> field){
        if (nameFieldTypes.stream().noneMatch(c -> c.equals(field))) {
            nameFieldTypes.add(field);
        }
    }

    public void unregisterNameField(Class<NameField> field){
        nameFieldTypes.remove(field);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
    *                     NameField面板操作
    * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void addEmptyPane(){
        addPane(new NameFieldSelector());
    }

    public void addPane(Class<NameField> type){
        try {
            if (type!=null){
                addPane(type.getConstructor(type).newInstance());
            }
        }catch (Exception e){
            throw new MRTRuntimeException(e);
        }
    }

    public void addPane(NameField nameField){
        NameFieldPaneContainer container = new NameFieldPaneContainer
                (this.nameFieldPane.getChildren().size(), nameField);
        this.containerList.add(container);
        nameFieldPane.getChildren().add(container);
    }

    public void clearAllPane(){
        this.nameFieldPane.getChildren().clear();
        this.containerList.clear();
    }

    public void deletePane(@NotNull NameFieldPaneContainer container){
        container.setVisible(false);

        int index = container.getIndex();

        if (index != containerList.size()){
            for (int i = 0 ; i < (containerList.size()-index-1); i++) {
                exchangePaneLocation(container , index + i , index + i + 1 );
            }
        }

        this.containerList.remove(container);
        this.nameFieldPane.getChildren().remove(container);
    }

    public void moveUpPane(@NotNull NameFieldPaneContainer container){
        int i = container.getIndex();
        int j = i - 1 ;
        if (j != -1){
            exchangePaneLocation(container, i, j);
        }
    }

    public void moveDownPane(@NotNull NameFieldPaneContainer container){
        int i = container.getIndex();
        int j = i + 1 ;
        if (j <= this.containerList.size()){
            exchangePaneLocation(container , i , j);
        }
    }

    public void exchangePaneLocation(NameFieldPaneContainer container /* 要调整的container */,
                                     int oldLocation , int newLocation){
        if (container!=null){
            container.resetLocation(newLocation);
        }

        if (newLocation<this.containerList.size()){
            NameFieldPaneContainer jPane = this.containerList.get(newLocation);
            if (jPane!=null){
                jPane.resetLocation(oldLocation);
            }
        }


        Collections.swap(this.containerList, oldLocation, newLocation);
    }

    public static class NameFieldPaneContainer extends AnchorPane {

        protected int index ;

        protected NameField nameField ;

/*
        Label name ; //测试用
*/

        public NameFieldPaneContainer(int i , NameField field) {
            this.index = i ;
            this.nameField = field ;

            this.setWidth(500);
            this.setHeight(containerPaneHeight);

/*
            name = new Label("Pane-"+this.index);  //测试用
            name.setLayoutX(50);  //测试用
            this.getChildren().add(name);  //测试用
*/
            resetLayout();

            AnchorPane pane = field.getNameFieldPane();
            pane.setLayoutX(10);

            this.getChildren().add(pane);
            this.getChildren().add(createControlButton());
            this.getChildren().add(createSeparator());
        }

        public void resetLayout(){
            this.setLayoutY(index * containerPaneHeight);
        }

        public void resetLocation(int index){
            setIndex(index).resetLayout();
        }

        SplitMenuButton createControlButton(){
            SplitMenuButton controlButton = new SplitMenuButton();
            controlButton.setMinSize(24 , 20);
            controlButton.setPrefSize(0, 0);
            controlButton.setMaxSize(0 , 0);
            controlButton.setLayoutY(6);
            controlButton.setLayoutX(5);
            controlButton.getItems().addAll(
                    createMenuItem("删除", event -> NameFieldManager.getInstance().deletePane(this)),
                    createMenuItem("上移", event -> NameFieldManager.getInstance().moveUpPane(this)),
                    createMenuItem("下移", event -> NameFieldManager.getInstance().moveDownPane(this))
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

        public NameField getNameField() {
            return nameField;
        }

        public NameFieldPaneContainer setNameField(NameField nameField) {
            this.nameField = nameField;
            return this;
        }

        @Override
        public String toString() {
            return "NameFieldPaneContainer{" +
                    "index=" + index +
/*                    +", name=" + name.getText() +*/
                    '}';
        }
    }

}
