package com.github.lunarconcerto.magicalrenametool.core;

import com.github.lunarconcerto.magicalrenametool.Main;
import com.github.lunarconcerto.magicalrenametool.config.Configuration;
import com.github.lunarconcerto.magicalrenametool.config.ConfigurationLoader;
import com.github.lunarconcerto.magicalrenametool.rule.EmptyRule;
import com.github.lunarconcerto.magicalrenametool.rule.Rule;
import com.github.lunarconcerto.magicalrenametool.rule.impl.dlsite.onsei.DlsiteOnseiRule;
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
import java.net.URL;
import java.util.HashMap;

public class RenameToolApplication extends Application {

    public static final String NAME = "MRT 文件批量重命名工具";

    public static final String VERSION = "version-0.1";

    public static RenameToolController controller ;

    public static Configuration configuration ;

    public static HashMap<String, Rule> ruleMap = new HashMap<>();

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("magical-rename-tool.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        controller = fxmlLoader.getController();

        initStage(stage , scene);
        initAfterUILoad();
    }

    private static void initStage(@NotNull Stage stage, Scene scene) throws IOException {
        stage.setResizable(false);
        stage.setTitle(NAME + " —— " + VERSION);
        stage.setScene(scene);
        stage.setOnCloseRequest(RenameToolApplication::closeHandler);
        stage.getIcons().add(new Image(FileUtil.getResourceAsStream("cafe.png")));
        stage.show();
    }

    public static void initBeforeUILoad(){
        Configuration.initLogger();
        ConfigurationLoader loader = ConfigurationLoader.getLoader();
        configuration = loader.toConfiguration();

        Thread.setDefaultUncaughtExceptionHandler(RenameToolApplication::errorHandler);
    }

    private static void initAfterUILoad() throws IOException {
        configuration.initUI(controller);

        controller.getFile_tree().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        controller.getSelected_file_list().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        controller.getConsole_list().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        controller.image_cafe.setImage(new Image(FileUtil.getResourceAsStream("cafe.jpg")));
        controller.statusLabelSetDefault();
        configuration.setProxy();

        initRules();

        configuration.setSelectedRule();
    }

    private static void initRules(){
        ruleMap.put(EmptyRule.class.getName() , new EmptyRule());
        ruleMap.put(DlsiteOnseiRule.class.getName() , new DlsiteOnseiRule());

        for (Rule value : ruleMap.values()) {
            try {
                value.getSettingPanel();
            } catch (IOException e) {
                e.printStackTrace();
            }
            controller.addRule(value);

            value.loadSettings(configuration.getProperties());
        }
    }

    private static void saveRuleSettings(){
        for (Rule value : ruleMap.values()) {
            value.saveSettings(configuration.getProperties());
        }
    }

    private static void closeHandler(WindowEvent event){
        saveRuleSettings();

        ConfigurationLoader.getLoader().save(configuration);
    }

    private static void errorHandler(Thread t, @NotNull Throwable e){
        String text = buildErrorText(e);
        printToUIConsole(text);

        e.printStackTrace();
    }

    private static @NotNull String buildErrorText(@NotNull Throwable e){
        StringBuilder builder = new StringBuilder();
        builder.append("程序发生异常！").append("\n")
                .append("发生").append(e.getMessage()).append("\n");
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement traceElement : stackTrace) {
            builder.append("在 ").append(traceElement).append("\n");
        }
        builder.append("请百度搜索问题原因或给我提交issue");

        return builder.toString() ;
    }

    public static void printToUIConsole(String text){
        if (controller!=null) {
            controller.printToUIConsole(text);
        }
    }
}