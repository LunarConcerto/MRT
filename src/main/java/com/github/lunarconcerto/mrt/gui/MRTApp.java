package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.MRTStarter;
import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.exc.MRTException;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.RuleManager;
import com.github.lunarconcerto.mrt.rule.RuleSettingPreset;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MRTApp extends Application {

    public static final String NAME = "MRT.文件批量重命名工具";

    public static final String VERSION = "[v]1.0";

    public static MRTController mainController ;

    public static Configuration configuration ;

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MRTStarter.class.getResource("mrt.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        mainController = fxmlLoader.getController();
        initStage(stage , scene);
        initAfterUILoad();
    }

    private static void initStage(@NotNull Stage stage, Scene scene) throws IOException {
        stage.setResizable(false);
        stage.setTitle(NAME + "    " + VERSION);
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

        RuleManager.getInstance();
    }

    private static void initAfterUILoad() throws IOException {
        mainController.init();

        configuration.applyConfig();
    }


    private static void closeHandler(WindowEvent event){
        savePreset();

        ConfigurationManager.getManager().save(configuration);
    }

    private static void savePreset(){
        List<RuleDefiner> fillingRules = mainController.ruleFillingSetter.getItems();

        List<RuleDefiner> collect = new ArrayList<>();
        if (!fillingRules.isEmpty()){
            collect.addAll(fillingRules);
        }

        List<RuleDefiner> replaceRules = mainController.ruleReplaceSetter.getItems();
        if (!replaceRules.isEmpty()){
            collect.addAll(replaceRules);
        }

        RuleSettingPreset defaultPreset = RuleSettingPreset.createNewPreset("default", collect);
        ConfigurationManager.getManager().addPreset(defaultPreset);
    }


    public static void printToUiLogger(String text){
        if (mainController !=null) {
            mainController.printToUILogger(text);
        }
    }
}