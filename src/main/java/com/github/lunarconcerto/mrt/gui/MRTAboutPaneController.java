package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MRTAboutPaneController extends AnchorPane {

    @FXML
    protected ImageView imageCafe;


    public MRTAboutPaneController() {

    }

    void init() throws IOException {
        this.imageCafe.setImage(new Image(FileUtil.getResourceAsStream("cafe.jpg")));

    }

    /*
     * 关于页面触发
     * */

    @FXML
    public void onLink1(){
        String url = "https://github.com/LunarConcerto/AutoRenameToolForDoujinOnsei";

        try {
            Dialog<String> dialog = ClipboardDialog.getDialog(url);

            dialog.show();
        } catch (IOException e) {
            throw new MRTRuntimeException(e);
        }
    }

    @FXML
    public void onLink2(){
        String url = "https://github.com/LunarConcerto/";

        try {
            Dialog<String> dialog = ClipboardDialog.getDialog(url);

            dialog.show();
        } catch (IOException e) {
            throw new MRTRuntimeException(e);
        }
    }


    public static void showWindow(){
        try {
            MRTAboutPaneController controller = new MRTAboutPaneController();
            Parent parent = ControllerUtil.loadWindow(controller, "mrt.about.fxml");

            controller.init();
            Stage stage = ControllerUtil.newStage(parent, "关于", 800, 600);

            stage.getIcons().add(new Image(FileUtil.getResourceAsStream("icon.cafe.png")));
            stage.show();
        }catch (Exception e){
            throw new MRTRuntimeException(e);
        }
    }

}
