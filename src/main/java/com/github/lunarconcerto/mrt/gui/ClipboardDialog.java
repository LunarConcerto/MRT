package com.github.lunarconcerto.mrt.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ClipboardDialog extends AnchorPane {

    @FXML
    public TextArea text ;

    public ClipboardDialog() {}

    public static @NotNull Dialog<String> getDialog(String s) throws IOException {
        ClipboardDialog dialog = ClipboardDialog.load(s);

        Dialog<String> stringDialog = new Dialog<>();

        stringDialog.setTitle("复制到剪切板");
        stringDialog.getDialogPane().setContent(dialog);

        ButtonType button = new ButtonType("确定");

        stringDialog.getDialogPane().getButtonTypes().add(button);
        stringDialog.setWidth(400);
        stringDialog.setHeight(300);

        return stringDialog ;
    }

    public static @NotNull ClipboardDialog load(String s) throws IOException {
        ClipboardDialog dialog = new ClipboardDialog();

        FXMLLoader fxmlLoader = new FXMLLoader(ClipboardDialog.class.getResource("clipboard.fxml"));
        fxmlLoader.setRoot(dialog);
        fxmlLoader.setController(dialog);
        fxmlLoader.load() ;

        dialog.text.setText(s);

        return dialog ;
    }
}
