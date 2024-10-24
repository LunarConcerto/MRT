package com.github.lunarconcerto.mrt.controller;

import com.github.lunarconcerto.mrt.MRTApplication;
import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.config.PreferencePaneManager;
import com.github.lunarconcerto.mrt.control.DirectoryCheckTreeView;
import com.github.lunarconcerto.mrt.control.RuleDefiningListView;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.exc.MRTRuleException;
import com.github.lunarconcerto.mrt.model.LogMessage;
import com.github.lunarconcerto.mrt.model.Model;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.io.PresetLoader;
import com.github.lunarconcerto.mrt.rule.io.RuleDefinerPreset;
import com.github.lunarconcerto.mrt.rule.io.SerializableRulePreset;
import com.github.lunarconcerto.mrt.task.FileRenameTask;
import com.github.lunarconcerto.mrt.util.Exit;
import com.github.lunarconcerto.mrt.util.Lists;
import com.github.lunarconcerto.mrt.util.Logger;
import com.github.lunarconcerto.mrt.util.Texts;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.controlsfx.control.MasterDetailPane;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 主面板控制器
 */
@Data
@NoArgsConstructor
public class IndexController implements Initializable {

    /**
     * 当前选择的路径
     */
    protected File selectedPath;

    protected Configuration configuration;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 控件定义
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    protected VBox mainBox;

    /**
     * 水平分割面板
     * <p>
     * 是 {@link #directoryTree} 和 {@link #ruleList} 的容器
     */
    @FXML
    protected SplitPane splitHorizontal;

    /**
     * 竖直分割面板
     * <p>
     * 是 {@link #splitHorizontal} 和 {@link #logger} 的容器
     */
    @FXML
    protected SplitPane splitVertical;

    /**
     * 定义文件名构建规则
     */
    @FXML
    protected RuleDefiningListView ruleList;

    /**
     * 展示当前打开的目录下的所有文件/文件夹，以供选择。
     */
    @FXML
    protected DirectoryCheckTreeView directoryTree;

    @FXML
    protected MasterDetailPane logger;

    @FXML
    protected MFXTableView<LogMessage> logMessageTable;

    @FXML
    protected TextArea logDetail;

    /**
     * 状态标签
     */
    @FXML
    protected Label status;

    /**
     * 进度条
     */
    @FXML
    protected MFXProgressBar progressBar;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 工具栏
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * 工具栏
     */
    @FXML
    protected MenuBar menuBar;

    /**
     * 历史打开路径
     */
    @FXML
    protected Menu menuHistoryPath;

    /**
     * 仅显示文件夹
     */
    @FXML
    protected CheckMenuItem menuItemDirOnly;

    /**
     * 窗口置顶
     */
    @FXML
    protected CheckMenuItem menuItemAlwaysOnTop;

    /**
     * 隐藏日志
     */
    @FXML
    protected CheckMenuItem menuItemHideLogger;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 初始化
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configuration = MRTApplication.configuration;

        /* 初始化日志视图 */
        ObservableList<MFXTableColumn<LogMessage>> tableColumns = logMessageTable.getTableColumns();
        tableColumns.get(0).setRowCellFactory(logMessage -> new MFXTableRowCell<>(LogMessage::getTime));
        tableColumns.get(1).setRowCellFactory(logMessage -> new MFXTableRowCell<>(LogMessage::getLevel));
        tableColumns.get(2).setRowCellFactory(logMessage -> new MFXTableRowCell<>(LogMessage::getMessage));
        logMessageTable.getSelectionModel().selectionProperty().addListener((observable, oldValue, newValue) ->
                logDetail.setText(logMessageTable.getSelectionModel().getSelectedValues().get(0).getDetail()));
        logMessageTable.autosizeColumnsOnInitialization();
        Model.logMessages.add(LogMessage.info("初始化完成."));
        logMessageTable.setItems(Model.logMessages);

        /* 初始化目录树视图 */
        directoryTree.setOnOpenDirectory(event -> {
            changeStatusLabel("读取目录...");
        }).setOnOpenDirectoryComplete(event -> {
            statusLabelSetDefault();
            Logger.info("读取了" + event.getLoadedFileCount() + "个文件/文件夹.");
        });
        directoryTree.targetDirectoryProperty().addListener((observable, oldValue, newValue) -> {
            selectedPath = newValue;
        });

        /* 初始化仅显示文件夹按钮 */
        menuItemDirOnly.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setDirShowOnly(newValue);
            directoryTree.setDirShowOnly(newValue);
        });

        /* 初始化窗口置顶按钮 */
        menuItemAlwaysOnTop.setGraphic(Texts.ALWAYS_ON_TOP_OFF);
        menuItemAlwaysOnTop.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setAlwaysOnTop(newValue);
            MRTApplication.mainStage.setAlwaysOnTop(newValue);
            if (newValue) {
                menuItemAlwaysOnTop.setGraphic(Texts.ALWAYS_ON_TOP_OFF);
            } else {
                menuItemAlwaysOnTop.setGraphic(Texts.ALWAYS_ON_TOP_ON);
            }
        });

        /* 隐藏日志 */
        menuItemHideLogger.setGraphic(Texts.HIDE_LOGGER_OFF);
        menuItemHideLogger.selectedProperty().addListener((observable, oldValue, newValue) -> {
            configuration.setHideLogger(newValue);
            if (newValue) {
                splitVertical.getItems().remove(logger);

                menuItemHideLogger.setGraphic(Texts.HIDE_LOGGER_ON);
            } else {
                splitVertical.getItems().add(logger);
                splitVertical.setDividerPositions(0.7, 0.3);

                menuItemHideLogger.setGraphic(Texts.HIDE_LOGGER_OFF);
            }
        });

        /* 其他初始化 */
        updateFileTree();
        loadHistoryPath();
        loadDefaultPreset();
    }

    private void loadHistoryPath() {
        menuHistoryPath.getItems().clear();

        List<String> paths = ConfigurationManager.getManager().getConfiguration().getHistoryPaths();
        if (paths != null) {
            paths.forEach(this::addHistoryPath);
        }
    }

    private void loadDefaultPreset() {
        List<SerializableRulePreset> presetList = ConfigurationManager.getManager().getPresetList();

        presetList.stream()
                .filter(preset -> preset.getPresetName().equals("default"))
                .findFirst()
                .ifPresent(this::loadFromPreset);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 工具栏触发
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    protected void onMenuItemSelectFilePathAction() {
        File choosingFile = selectedPath != null ?
                WindowManager.createNewDirectoryChooser(selectedPath) :
                WindowManager.createNewDirectoryChooser();

        if (choosingFile == null) return;

        selectedPath = choosingFile;
        recordHistoryPath(choosingFile.getPath());
        buildTree();
    }

    @FXML
    protected void onMenuItemOpenDefaultFilePath() {
        selectedPath = new File(ConfigurationManager.getManager().getConfiguration().getDefaultPath());
        if (selectedPath.exists()) {
            buildTree();
        } else {
            WindowManager.showError("打开路径失败",
                    "默认的文件路径不存在，您的路径设置可能有误，请检查设置...->系统设置->文件->默认打开路径。");
        }
    }

    @FXML
    protected void onMenuItemAboutAction(ActionEvent actionEvent) {
        WindowManager.showAbout();
    }

    @FXML
    protected void onMenuItemPropertyAction(ActionEvent actionEvent) {
        PreferencePaneManager.get().showPreferences();
    }

    @FXML
    protected void onMenuItemExitAction(ActionEvent actionEvent) {
        Exit.exit();
    }

    @FXML
    protected void onMenuItemLoadPreset() {
        WindowManager.showPresetLoadSelector();
    }

    @FXML
    protected void onMenuItemDeletePreset() {
        WindowManager.showPresetDeleteSelector();
    }

    @FXML
    protected void onMenuItemSavePreset() {
        String header = "保存预设";

        String input = WindowManager.showTextInput(header,
                "请为该预设起一个名字 :",
                "preset" + ConfigurationManager.getManager().getPresetList().size());

        if (input == null) {
            WindowManager.showInformation(header, "取消保存.");
            return;
        }

        ConfigurationManager.getManager().addPreset(saveRuleToPreset(input));

        WindowManager.showInformation(header, "保存成功！");
    }

    @FXML
    protected void onMenuItemRunProgress() {
        if (directoryTree.getCheckedItems().isEmpty()) {
            WindowManager.showError("运行失败", "不能开始运行，因为您未指定要进行处理的文件/文件夹。");
            return;
        }

        if (ruleList.getItems().isEmpty()) {
            WindowManager.showError("运行失败", "不能开始运行，因为您未指定构建新文件名的规则和操作。");
            return;
        }

        startNewTask(directoryTree.getCheckedFiles(), ruleList.getNameEditors());
    }

    @FXML
    //TODO: RepealChange
    protected void onMenuItemRepealChange(ActionEvent actionEvent) {
        WindowManager.showInformation("敬请期待", "该功能尚未完成");
    }

    public void onMenuItemHelp(ActionEvent actionEvent) {
        WindowManager.showHelp();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 规则定义面板
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void addRule(Rule rule) {
        addRule(rule, -1);
    }

    public void addRule(@NotNull Rule rule, int index) {
        RuleDefiningPane definer = rule.createDefiningPane();
        if (definer != null) {
            List<RuleDefiningPane> viewItems = ruleList.getItems();
            if (index >= 0) {
                viewItems.add(index, definer);
            } else {
                viewItems.add(definer);
            }
        } else {
            throw new MRTRuleException(MRTRuleException.ErrorType.VER_UNDEFINED,
                    rule, "规则定义器(RuleDefiner)为空.");
        }
    }

    void clearRulePane(@NotNull ListView<RuleDefiningPane> listView) {
        listView.getItems().clear();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 实用操作
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void startNewTask(List<File> files, List<NameEditor> editors) {
        lock();
        FileRenameTask.newTask(files, progressBar, editors);
    }

    public void lock() {
        splitHorizontal.setDisable(true);
        menuBar.setDisable(true);
    }

    public void unlock() {
        splitHorizontal.setDisable(false);
        menuBar.setDisable(false);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 底部状态栏操作
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void changeStatusLabel(String text) {
        Platform.runLater(() -> status.setText(text));
    }

    public void statusLabelSetDefault() {
        Platform.runLater(() -> status.setText("就绪"));
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 读取 / 保存
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    void loadFromPreset(@NotNull SerializableRulePreset preset) {
        RuleDefinerPreset definerPreset = PresetLoader.load(preset);

        definerPreset.addToListView(ruleList);
    }

    public SerializableRulePreset saveRuleToPreset(String presetName) {
        List<RuleDefiningPane> collect = new ArrayList<>();

        if (!ruleList.getItems().isEmpty()) collect.addAll(ruleList.getItems());

        return SerializableRulePreset.createNewPreset(presetName, collect);
    }

    private void recordHistoryPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }

        ConfigurationManager.getManager()
                .getConfiguration()
                .addHistoryPath(path);

        loadHistoryPath();
    }

    public void addHistoryPath(String path) {
        MenuItem item = new MenuItem(path);
        item.setOnAction(event -> {
            selectedPath = new File(item.getText());
            buildTree();


            Lists.moveToFirst(menuHistoryPath.getItems(), item);
            Lists.moveToFirst(configuration.getHistoryPaths(), path);
        });


        menuHistoryPath.getItems().add(item);
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 重置 / 更新
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void reset() {
        statusLabelSetDefault();
        progressBar.setProgress(0);
        directoryTree.reload();
    }

    public void updateFileTree() {
        if (this.selectedPath == null || !selectedPath.exists()) {
            this.setSelectedPath(new File(MRTApplication.configuration.getDefaultPath()));
        }

        this.buildTree();
    }

    public void buildTree() {
        if (selectedPath != null) {
            directoryTree.setTargetDirectory(selectedPath);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * UI日志输出
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void info(String text, String... details) {
        Platform.runLater(() -> logger(text, LogMessage.Level.INFO, details));
    }

    public void warn(String text, String... details) {
        Platform.runLater(() -> logger(text, LogMessage.Level.WARN, details));
    }

    public void error(String text, String... details) {
        Platform.runLater(() -> logger(text, LogMessage.Level.ERROR, details));
    }

    protected void logger(String text, LogMessage.@NotNull Level level, String... details) {
        switch (level) {
            case DEBUG -> Model.logMessages.add(LogMessage.debug(text, details));
            case INFO -> Model.logMessages.add(LogMessage.info(text, details));
            case WARN -> Model.logMessages.add(LogMessage.warn(text, details));
            case ERROR -> Model.logMessages.add(LogMessage.error(text, details));
            default -> Model.logMessages.add(LogMessage.info(text, details));
        }

        logMessageTable.scrollToLast();
        logMessageTable.getSelectionModel().getSelection().clear();
        logMessageTable.getSelectionModel().selectIndex(logMessageTable.getItems().size() - 1);
    }

}