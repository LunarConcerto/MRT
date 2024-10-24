package com.github.lunarconcerto.mrt;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.controller.IndexController;
import com.github.lunarconcerto.mrt.controller.TrayIconManager;
import com.github.lunarconcerto.mrt.util.Exit;
import com.github.lunarconcerto.mrt.util.FileUtil;
import com.github.lunarconcerto.mrt.util.I18n;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@Slf4j
public class MRTApplication extends Application {

    public static final String VERSION = "[v]1.1.0";

    public static IndexController mainController;

    public static Stage mainStage;

    public static Configuration configuration;

    public static HostServices hostServices;

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        configuration.checkHistoryPathsExistAndRemove();
        /* 读取FXML */
        FXMLLoader fxmlLoader = new FXMLLoader(MRTStarter.class.getResource("fxml/index.fxml"));
        fxmlLoader.setResources(I18n.RESOURCE_BUNDLE);
        Scene scene = new Scene(fxmlLoader.load());
        mainController = fxmlLoader.getController();
        /* 创建Stage */
        mainStage = stage;
        hostServices = getHostServices();
        stage.setTitle(I18n.get("mrt.name") + "\t" + VERSION);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            if (configuration.isExitOnStageClose()) {
                Exit.exit();
            } else {
                stage.hide();
                event.consume();
            }
        });
        stage.getIcons().add(FileUtil.icon);
        stage.show();
        /* 创建托盘图标 */
        TrayIconManager.getTrayIconManager().initTrayIcon(stage);

        /* 应用设置 */
        configuration.initProxy();
        configuration.updateUI();

        applyConfig();
        test();
    }

    private void applyConfig() {
        mainController.getDirectoryTree().setDirShowOnly(configuration.isDirShowOnly());
        mainController.getDirectoryTree().setDepth(configuration.getDirectoryTreeDepth());
        mainController.getMenuItemDirOnly().setSelected(configuration.isDirShowOnly());
        mainController.getMenuItemAlwaysOnTop().setSelected(configuration.isAlwaysOnTop());
        mainController.getMenuItemHideLogger().setSelected(configuration.isHideLogger());
    }

    private void test() {

    }

}