package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.MRTStarter;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;

class ControllerUtil {

    static Parent loadWindow(AnchorPane controller, String name){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MRTStarter.class.getResource(name));
            fxmlLoader.setRoot(controller);
            fxmlLoader.setController(controller);

            return fxmlLoader.load();
        } catch (Exception e) {
            throw new MRTRuntimeException(e);
        }
    }

    static File createNewDirectoryChooser(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择一个文件夹");
        return chooser.showDialog(new Stage());
    }

    static File createNewDirectoryChooser(File file){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择一个文件夹");
        chooser.setInitialDirectory(file);
        return chooser.showDialog(new Stage());
    }

    static @NotNull Stage newStage(Parent parent, String title){
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(parent));
        return stage ;
    }

    static @NotNull Stage newStage(Parent parent, String title, int width, int height){
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(parent, width, height));
        return stage ;
    }

}
