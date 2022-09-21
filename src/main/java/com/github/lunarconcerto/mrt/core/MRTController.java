package com.github.lunarconcerto.mrt.core;

import com.github.lunarconcerto.mrt.component.*;
import com.github.lunarconcerto.mrt.util.FileNode;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.lunarconcerto.mrt.core.ControllerUtil.createNewDirectoryChooser;

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

    // UI日志
    @FXML
    protected ListView<String> uiLogger;

    // [选择文件夹]按钮
    @FXML
    protected MenuItem menuItemSelectFile;

    // [运行]按钮
    @FXML
    protected MenuItem menuItemStartProgress;

    // 左侧显示文件层次的树视图
    @FXML
    protected TreeView<FileNode> treeViewFileSelector;

    // 中间上方显示已显示文件的列表视图
    @FXML
    protected ListView<FileNode> listViewSelectedFiles;

    // 左下方显示当前状态的标签
    @FXML
    protected Label labelStatusLeft;

    // 中间进度条
    @FXML
    protected ProgressBar progressBar;

    // 文件树视图中右键选择的[仅显示文件夹]菜单按钮
    @FXML
    protected CheckMenuItem menuItemDirOnly;

    //填充型规则定义表
    @FXML
    public ListView<AnchorPane> ruleFillingSetter;

    //替换型规则定义表
    @FXML
    public ListView<AnchorPane> ruleReplaceSetter;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public MRTController() {}

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 顶部菜单栏按钮触发方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

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
        addAllToList(nodeList);
    }

    @FXML
    public void onTreeMenuSelectAllAction(){
        List<FileNode> fileNodeList = getRootChildrenNodeList();
        addAllToList(fileNodeList);
        treeViewFileSelector.getSelectionModel().selectAll();
    }

    @FXML
    public void onTreeMenuReverseSelectAction(){
        List<FileNode> targetList = getRootChildrenNodeList()
                .stream()
                .filter(fileNode -> !getSelectedTreeNodeList().contains(fileNode))
                .collect(Collectors.toList());
        addAllToList(targetList);
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

        listViewSelectedFiles.refresh();
    }

    @FXML
    public void onListMenuRemoveAllAction(){
        listViewSelectedFiles.getItems().clear();

        listViewSelectedFiles.refresh();
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

    private void addAllToList(@NotNull List<FileNode> fileNodeList){
        ObservableList<FileNode> listItems = listViewSelectedFiles.getItems();
        int addedItems = 0;
        for (FileNode fileNode : fileNodeList) {
            boolean exists = false ;
            for (FileNode listItem : listItems) {
                if (fileNode==listItem){
                    exists = true ;
                    break;
                }
            }
            if (!exists){
                listItems.add(fileNode);
                addedItems++ ;
            }
        }
        printToUILogger("将%s个文件加入到待处理列表".formatted(addedItems));
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

}