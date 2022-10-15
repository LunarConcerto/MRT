package com.github.lunarconcerto.mrt.gui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.StageStyle;
import org.controlsfx.dialog.CommandLinksDialog;

import java.util.Optional;

public class Dialogs {

    private Dialogs() {}

    public static void showInformation(String header, String message){
        showDialog("提示" , header, message , Alert.AlertType.INFORMATION);
    }

    public static void showWarning(String header, String message){
        showDialog("警告" , header, message , Alert.AlertType.WARNING);
    }

    public static void showError(String header, String message){
        showDialog("错误" , header, message , Alert.AlertType.ERROR);
    }

    public static void showDialog(String title , String header, String message , Alert.AlertType type){
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);

            alert.show();
        });
    }

    public static ButtonType showCommandLinks(String header, String message , ButtonType... buttons){
        CommandLinksDialog dialog = new CommandLinksDialog();
        dialog.setTitle("选择");
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().addAll(buttons);
        return dialog.showAndWait().orElse(ButtonType.CLOSE);
    }

    public static String showTextInput(String header, String message , String defaultValue){
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("输入");
        dialog.setHeaderText(header);
        dialog.setContentText(message);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

}
