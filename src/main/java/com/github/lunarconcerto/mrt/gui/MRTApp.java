package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.MRTStarter;
import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.exc.MRTException;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.application.Application;
import javafx.event.EventType;
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
        FXMLLoader fxmlLoader = new FXMLLoader(MRTStarter.class.getResource("mrt.fxml"));
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
        stage.getIcons().add(new Image(FileUtil.getResourceAsStream("icon.cafe.png")));
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
        controller.getTreeViewFileSelector().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        controller.getListViewSelectedFiles().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        controller.getUiLogger().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        controller.statusLabelSetDefault();
        configuration.applyConfig();

        controller.updateUI();

        controller.ruleFillingSetter.setFixedCellSize(40);
        controller.ruleReplaceSetter.setFixedCellSize(40);
    }


    private static void closeHandler(WindowEvent event){
        ConfigurationManager.getManager().save(configuration);
    }

    private static void errorHandler(Thread t, @NotNull Throwable e){
        printToUiLogger(MRTException.buildErrorText(e));
        e.printStackTrace();
    }


    public static void printToUiLogger(String text){
        if (controller!=null) {
            controller.printToUILogger(text);
        }
    }
}