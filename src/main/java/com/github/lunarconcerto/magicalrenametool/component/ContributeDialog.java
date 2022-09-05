package com.github.lunarconcerto.magicalrenametool.component;

import com.github.lunarconcerto.magicalrenametool.util.FileUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ContributeDialog extends AnchorPane {

    @FXML
    public ImageView image1 ;

    @FXML
    public ImageView image2 ;

    public ContributeDialog() {
    }

    public static @NotNull Dialog<String> getDialog() throws IOException {
        ContributeDialog pane = ContributeDialog.load();

        pane.image1.setImage(new Image(FileUtil.getResourceAsStream("money_code_wechat.png")));
        pane.image2.setImage(new Image(FileUtil.getResourceAsStream("money_code_zhifubao.png")));

        Dialog<String> stringDialog = new Dialog<>();

        stringDialog.setTitle("捐赠");
        stringDialog.getDialogPane().setContent(pane);

        ButtonType button = new ButtonType("好的");

        stringDialog.getDialogPane().getButtonTypes().add(button);
        stringDialog.setWidth(600);
        stringDialog.setHeight(500);

        return stringDialog ;
    }

    public static @NotNull ContributeDialog load() throws IOException {
        ContributeDialog dialog = new ContributeDialog();

        FXMLLoader fxmlLoader = new FXMLLoader(ContributeDialog.class.getResource("contribute.fxml"));
        fxmlLoader.setRoot(dialog);
        fxmlLoader.setController(dialog);
        fxmlLoader.load() ;


        return dialog ;
    }

}
