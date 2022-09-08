package com.github.lunarconcerto.magicalrenametool.core;

import com.github.lunarconcerto.magicalrenametool.MRTStarter;
import com.github.lunarconcerto.magicalrenametool.config.Configuration;
import com.github.lunarconcerto.magicalrenametool.config.ConfigurationManager;
import com.github.lunarconcerto.magicalrenametool.exc.MRTException;
import com.github.lunarconcerto.magicalrenametool.component.Preview;
import com.github.lunarconcerto.magicalrenametool.field.NameFieldManager;
import com.github.lunarconcerto.magicalrenametool.util.FileUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MRTApp extends Application {

    public static final String NAME = "MRT 文件批量重命名工具";

    public static final String VERSION = "version-0.2";

    public static MRTController controller ;

    public static Configuration configuration ;

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MRTStarter.class.getResource("magical-rename-tool.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        controller = fxmlLoader.getController();

        initStage(stage , scene);
        initAfterUILoad();
    }

    private static void initStage(@NotNull Stage stage, Scene scene) throws IOException {
        stage.setResizable(false);
        stage.setTitle(NAME + " —— " + VERSION);
        stage.setScene(scene);
        stage.setOnCloseRequest(MRTApp::closeHandler);
        stage.getIcons().add(new Image(FileUtil.getResourceAsStream("cafe.png")));
        stage.show();
    }

    public static void initBeforeUILoad(){
        Configuration.initLogger();
        ConfigurationManager manager = ConfigurationManager.getManager();
        manager.load();
        configuration = manager.getConfiguration() ;

        Thread.setDefaultUncaughtExceptionHandler(MRTApp::errorHandler);
    }

    private static void initAfterUILoad() throws IOException {
        controller.getFile_tree().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        controller.getSelected_file_list().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        controller.getConsole_list().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        controller.image_cafe.setImage(new Image(FileUtil.getResourceAsStream("cafe.jpg")));
        controller.statusLabelSetDefault();
        configuration.enableProxy();

        NameFieldManager.getInstance().addEmptyPane();
        NameFieldManager.getInstance().addEmptyPane();

        controller.updateUI();
    }

    private static void closeHandler(WindowEvent event){
        controller.collectSetting();

        ConfigurationManager.getManager().save(configuration);
        Preview.getPreview().close();
    }

    private static void errorHandler(Thread t, @NotNull Throwable e){
        printToUIConsole(MRTException.buildErrorText(e));
        e.printStackTrace();
    }


    public static void printToUIConsole(String text){
        if (controller!=null) {
            controller.printToUIConsole(text);
        }
    }
}