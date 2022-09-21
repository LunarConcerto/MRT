package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.component.*;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.util.FileNode;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.Data;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.lunarconcerto.mrt.gui.ControllerUtil.createNewDirectoryChooser;

/**
 * 主面板控制器
 */
@Data
@Log4j(topic = "controller")
public class MRTController {

    /**
     * 当前选择的路径
     */
    protected FileNode selectingPath ;

    protected FileTreeBuilder fileTreeBuilder ;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 控件
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * 左侧，树视图
     * 展示当前打开的目录下的所有文件/文件夹，以供选择。
     */
    @FXML
    protected TreeView<FileNode> treeViewFileSelector;

    /**
     * 中上，列表视图，
     * 相当于等待进行重命名处理的文件列表。
     */
    @FXML
    protected ListView<FileContainer> listViewSelectedFiles;

    /**
     * 中下，列表视图
     * 用来代替命令行的UI日志显示。
     */
    @FXML
    protected ListView<String> uiLogger;

    /**
     * 左底，标签
     * 显示当前应用程序状态用。
     */
    @FXML
    protected Label labelStatusLeft;

    /**
     * 中底，进度条
     */
    @FXML
    protected ProgressBar progressBar;

    /**
     * 右上，列表视图
     * 用来定义[填充]类型的文件名规则
     *
     * @see com.github.lunarconcerto.mrt.rule.RuleType
     */
    @FXML
    public ListView<RuleDefiner> ruleFillingSetter;

    /**
     * 右下，列表视图
     * 用来定义[替换]类型的文件名规则
     *
     * @see com.github.lunarconcerto.mrt.rule.RuleType
     */
    @FXML
    public ListView<RuleDefiner> ruleReplaceSetter;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 菜单项
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * 历史打开路径按钮
     */
    @FXML
    protected Menu menuHistoryPath;

    /**
     * 树视图中仅显示文件夹
     */
    @FXML
    protected CheckMenuItem menuItemDirOnly;

    /**
     * 打开目录
     */
    @FXML
    protected MenuItem menuItemSelectFile;

    /**
     * 开始运行
     */
    @FXML
    protected MenuItem menuItemStartProgress;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public MRTController() {}

    protected void init(){
        getTreeViewFileSelector().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getListViewSelectedFiles().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getUiLogger().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        statusLabelSetDefault();

        updateUI();

        ruleFillingSetter.setFixedCellSize(40);
        ruleReplaceSetter.setFixedCellSize(40);

        loadHistoryPath();
    }

    private void loadHistoryPath(){
        menuHistoryPath.getItems().clear();

        List<String> paths = ConfigurationManager.getManager().getConfiguration().getHistoryPaths();
        if (paths!=null){
            paths.forEach(this::addHistoryPath);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 顶部菜单栏按钮触发方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public void onHistoryPathMenuShow(Event event) {

    }

    @FXML
    public void onMenuItemAboutAction(ActionEvent actionEvent) {
        MRTAboutPaneController.showWindow();
    }

    @FXML
    public void onMenuItemPropertyAction(ActionEvent actionEvent) {
        MRTPropertyPaneController.showWindow();
    }

    @FXML
    public void onMenuItemExitAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    public void onMenuItemSelectFilePathAction(){
        File choosingFile = selectingPath != null ? createNewDirectoryChooser(selectingPath) : createNewDirectoryChooser();

        if (choosingFile==null) return;

        selectingPath = new FileNode(choosingFile);
        log.debug("选择的文件路径为:" + selectingPath.getFullName() );
        recordHistoryPath(choosingFile.getPath());
        buildTree();
    }

    @FXML
    public void onStartButtonAction(){

    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 树视图(选择文件界面)中的相关操作
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public void onTreeMenuSelectAction(){
        List<FileNode> nodeList = getSelectedTreeNodeList();
        addAllToSelectedList(nodeList);
    }

    @FXML
    public void onTreeMenuSelectAllAction(){
        List<FileNode> fileNodeList = getRootChildrenNodeList();
        addAllToSelectedList(fileNodeList);
        treeViewFileSelector.getSelectionModel().selectAll();
    }

    @FXML
    public void onTreeMenuReverseSelectAction(){
        List<FileNode> targetList = getRootChildrenNodeList()
                .stream()
                .filter(fileNode -> !getSelectedTreeNodeList().contains(fileNode))
                .collect(Collectors.toList());
        addAllToSelectedList(targetList);
    }

    @FXML
    public void onTreeMenuRefreshAction(){
        buildTree();
    }

    @FXML
    public void onDirOnlyButtonAction(){
        MRTApp.configuration.setDirShowOnly(menuItemDirOnly.isSelected());
        updateUI();
    }

    public void buildTree(){
        if (selectingPath!=null){
            fileTreeBuilder = new FileTreeBuilder(treeViewFileSelector, selectingPath);
            fileTreeBuilder.buildTree(selectingPath);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 已选文件列表控件操作
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public void onListMenuRemoveAction(){
        listViewSelectedFiles.getItems().removeAll(listViewSelectedFiles.getSelectionModel().getSelectedItems());

        refreshSelectedFileIndex();
    }

    @FXML
    public void onListMenuRemoveAllAction(){
        listViewSelectedFiles.getItems().clear();

        refreshSelectedFileIndex();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * UI日志的操作
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public void onUILoggerCopy(){
        String s = uiLogger.getSelectionModel().getSelectedItems().stream().toList().toString();

        try {
            Dialog<String> dialog = ClipboardDialog.getDialog(s);

            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 其他控件操作
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void changeStatusLabel(String text){
        Platform.runLater(() -> labelStatusLeft.setText(text));
    }

    public void statusLabelSetDefault(){
        Platform.runLater(() -> labelStatusLeft.setText("就绪"));
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 其他方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void updateUI(){
        this.setSelectingPath(new FileNode(new File(MRTApp.configuration.getDefaultPath())));
        this.buildTree();
    }

    public void reset(){
        statusLabelSetDefault();
        listViewSelectedFiles.getItems().clear();
        progressBar.setProgress(0);

        buildTree();
    }

    private void addAllToSelectedList(@NotNull List<FileNode> fileNodeList){
        ObservableList<FileContainer> listItems = listViewSelectedFiles.getItems();
        int addedItems = 0;
        for (FileNode fileNode : fileNodeList) {
            boolean exists = false ;
            for (FileContainer listItem : listItems) {
                if (fileNode==listItem.node){
                    exists = true ;
                    break;
                }
            }
            if (!exists){
                listItems.add(new FileContainer(listItems.size(), fileNode));
                addedItems++ ;
            }
        }

        refreshSelectedFileIndex();
        printToUILogger("将%s个项目加入到待处理列表".formatted(addedItems));
    }

    private void recordHistoryPath(String path){
        File file = new File(path);
        if (!file.exists()){
            return;
        }

        ConfigurationManager.getManager().getConfiguration().addHistoryPath(path);
        loadHistoryPath();
    }

    public void addHistoryPath(String path){
        MenuItem item = new MenuItem(path);
        item.setOnAction(event -> {
            selectingPath = new FileNode(item.getText());
            buildTree();
        });
        menuHistoryPath.getItems().add(0, item);
    }

    private void refreshSelectedFileIndex(){
        listViewSelectedFiles.getItems()
                .forEach(item -> item.setIndex(listViewSelectedFiles.getItems().indexOf(item)));
        listViewSelectedFiles.refresh();
    }

    private boolean isChild(@NotNull TreeItem<FileNode> parent , @NotNull TreeItem<FileNode> child){
        if (parent.getValue().getLevel()==child.getValue().getLevel()-1){
            return parent.getChildren().contains(child) ;
        }
        return false ;
    }

    private @NotNull List<FileNode> getRootChildrenNodeList(){
        return treeItemListToFileNodeList(treeViewFileSelector.getRoot().getChildren()) ;
    }

    private @NotNull List<FileNode> getSelectedTreeNodeList(){
        List<TreeItem<FileNode>> treeItemList =
                treeViewFileSelector.getSelectionModel()
                .getSelectedItems()
                .stream()
                .toList();
        return treeItemListToFileNodeList(treeItemList) ;
    }

    private @NotNull List<FileNode> treeItemListToFileNodeList(@NotNull List<TreeItem<FileNode>> treeItemList){
        List<FileNode> result = new ArrayList<>(treeItemList.size());
        for (TreeItem<FileNode> selectedItem : treeItemList) {
            result.add(selectedItem.getValue());
        }
        return result ;
    }

    private @NotNull List<TreeItem<FileNode>> getSelectedTreeItemList(){
        return treeViewFileSelector
                .getSelectionModel()
                .getSelectedItems()
                .stream()
                .toList();
    }

    public static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    public void printToUILogger(String text){
        Platform.runLater(() -> {
            uiLogger.getItems().add(formatter.format(new Date()) + " : " + text + "\n");
            uiLogger.scrollTo(uiLogger.getItems().size());
        });
    }


    @Getter
    public static final class FileContainer {

        private int index;

        private final FileNode node;

        FileContainer(int index, FileNode node) {
            this.index = index;
            this.node = node;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        public @NotNull String toString() {
            return "[" + index + "]" + node.toString();
        }


    }

}