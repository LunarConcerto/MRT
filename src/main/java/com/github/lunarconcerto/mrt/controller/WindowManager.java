package com.github.lunarconcerto.mrt.controller;

import com.github.lunarconcerto.mrt.MRTStarter;
import com.github.lunarconcerto.mrt.task.RenameResult;
import com.github.lunarconcerto.mrt.util.FileUtil;
import com.github.lunarconcerto.mrt.util.I18n;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.dialog.CommandLinksDialog;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class WindowManager {

    private static final Map<String, Controller> exists = new HashMap<>();
    private static final ButtonType confirmType = new ButtonType("确认并继续 >>>", ButtonBar.ButtonData.RIGHT);
    private static final ButtonType cancelType = new ButtonType("<<< 清空并返回", ButtonBar.ButtonData.LEFT);

    public static void showHelp() {
        show("help.fxml", "帮助", 600, 600);
    }

    public static void showAbout() {
        show("about.fxml", "关于", 600, 400);
    }

    public static void showRuleSelector() {
        show("rule_selector.fxml", "选择规则", 600, 400);
    }

    public static void showPresetDeleteSelector() {
        Controller controller = show("preset_selector.fxml", "选择预设", 600, 600);
        ((PresetSelectorController) controller).setLoad(false);
    }

    public static void showPresetLoadSelector() {
        Controller controller = show("preset_selector.fxml", "选择预设", 600, 600);
        ((PresetSelectorController) controller).setLoad(true);
    }

    public static @NotNull Dialog<List<RenameResult>> showResultConfirmDialog(List<RenameResult> resultList) {
        try {
            ResultConfirmController controller = (ResultConfirmController) loadControl("result_confirm.fxml");
            controller.setResultList(resultList);
            controller.initTable();

            Dialog<List<RenameResult>> dialog = new Dialog<>();
            dialog.setTitle("确认当前生成的结果");
            dialog.getDialogPane().setContent(controller.getRoot());
            dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> dialog.close());
            dialog.getDialogPane().getButtonTypes().add(0, cancelType);
            dialog.getDialogPane().getButtonTypes().add(1, confirmType);
            dialog.setResultConverter(param -> param.getButtonData() == ButtonBar.ButtonData.RIGHT
                    ? controller.resultTable.getItems() : new ArrayList<>());

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
            stage.getIcons().add(FileUtil.icon);

            return dialog;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull Controller show(String name, String title, int width, int height) {
        if (exists.containsKey(name)) {
            Controller controller = exists.get(name);
            controller.show();
            return controller;
        } else try {
            Controller controller = loadWindow(name, title, width, height);
            exists.put(name, controller);
            controller.show();
            return controller;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 创建目录选择器
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public static File createNewDirectoryChooser() {
        return createNewDirectoryChooser(null);
    }

    public static File createNewDirectoryChooser(File file) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择一个文件夹");
        if (file != null && file.exists()) {
            chooser.setInitialDirectory(file);
        }
        Stage stage = new Stage();
        stage.setAlwaysOnTop(true);
        return chooser.showDialog(stage);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 读取FXML, 创建新窗口
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public static @NotNull Controller loadWindow(String fxmlName, String title, int preWidth, int preHeight) throws IOException {
        Controller controller = loadControl(fxmlName);
        controller.setStage(newStage(controller.getRoot(), title, preWidth, preHeight));
        return controller;
    }

    public static @NotNull Controller loadControl(String fxmlName) throws IOException {
        URL url = MRTStarter.class.getResource("fxml/" + fxmlName);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setResources(I18n.RESOURCE_BUNDLE);
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        controller.setFxmlLoader(fxmlLoader);
        controller.setRoot(root);
        controller.init();
        return controller;
    }

    public static @NotNull Stage newStage(Parent parent, String title, int width, int height) throws IOException {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.getIcons().add(FileUtil.icon);
        stage.setScene(new Scene(parent, width, height));
        return stage;
    }

    public static void showInformation(String header, String message) {
        showDialog("提示", header, message, Alert.AlertType.INFORMATION);
    }

    public static void showWarning(String header, String message) {
        showDialog("警告", header, message, Alert.AlertType.WARNING);
    }

    public static void showError(String header, String message) {
        showDialog("错误", header, message, Alert.AlertType.ERROR);
    }

    public static void showDialog(String title, String header, String message, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(message);
            ((Stage) alert.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
            alert.show();
        });
    }

    public static ButtonType showCommandLinks(String header, String message, ButtonType... buttons) {
        CommandLinksDialog dialog = new CommandLinksDialog();
        dialog.setTitle("选择");
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().addAll(buttons);
        ((Stage) dialog.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
        return dialog.showAndWait().orElse(ButtonType.CLOSE);
    }

    public static String showTextInput(String header, String message, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("输入");
        dialog.setHeaderText(header);
        dialog.setContentText(message);
        ((Stage) dialog.getDialogPane().getScene().getWindow()).setAlwaysOnTop(true);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

}
