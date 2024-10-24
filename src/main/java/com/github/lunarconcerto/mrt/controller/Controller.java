package com.github.lunarconcerto.mrt.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.Data;

@Data
public abstract class Controller {

    protected Stage stage;

    protected FXMLLoader fxmlLoader;

    protected Parent root;

    protected void init() {
    }

    public void show() {
        stage.show();
    }

    public void hide() {
        stage.hide();
    }

    public void close() {
        stage.close();
    }

}
