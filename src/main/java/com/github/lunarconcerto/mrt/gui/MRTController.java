package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.component.*;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.exc.MRTRuleException;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.RuleType;
import com.github.lunarconcerto.mrt.util.FileNode;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Data;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.github.lunarconcerto.mrt.gui.ControllerUtil.createNewDirectoryChooser;

/**
 * 主面板控制器
 */
@Data
@Log4j(topic = "controller")
public class MRTController {

    public static int ruleSetterListCellSize = 48 ;

    /**
     * 当前选择的路径
     */
    protected FileNode selectedPath;

    protected FileTreeBuilder fileTreeBuilder ;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 控件变量
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
     * 构造方法 / 初始化方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public MRTController() {}

    protected void init(){
        getTreeViewFileSelector().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getListViewSelectedFiles().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getUiLogger().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        statusLabelSetDefault();

        updateUI();

        ruleFillingSetter.setFixedCellSize(ruleSetterListCellSize);
        ruleReplaceSetter.setFixedCellSize(ruleSetterListCellSize);

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
     * FXML EventHandler method
     * 顶部菜单栏按钮
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public void onHistoryPathMenuShow(Event event) {

    }

    @FXML
    protected void onMenuItemAboutAction(ActionEvent actionEvent) {
        MRTAboutPaneController.showWindow();
    }

    @FXML
    protected void onMenuItemPropertyAction(ActionEvent actionEvent) {
        MRTPropertyPaneController.showWindow();
    }

    @FXML
    protected void onMenuItemExitAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    protected void onMenuItemSelectFilePathAction(){
        File choosingFile = selectedPath != null ? createNewDirectoryChooser(selectedPath) : createNewDirectoryChooser();

        if (choosingFile==null) return;

        selectedPath = new FileNode(choosingFile);
        log.debug("选择的文件路径为:" + selectedPath.getFullName() );
        recordHistoryPath(choosingFile.getPath());
        buildTree();
    }

    @FXML
    public void onStartButtonAction(){

    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * FXML EventHandler method
     * 树视图(选择文件界面)
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    protected void onTreeMenuSelectAction(){
        List<FileNode> nodeList = getSelectedTreeNodeList();
        addAllToSelectedList(nodeList);
    }

    @FXML
    protected void onTreeMenuSelectAllAction(){
        List<FileNode> fileNodeList = getRootChildrenNodeList();
        addAllToSelectedList(fileNodeList);
        treeViewFileSelector.getSelectionModel().selectAll();
    }

    @FXML
    protected void onTreeMenuReverseSelectAction(){
        List<FileNode> targetList = getRootChildrenNodeList()
                .stream()
                .filter(fileNode -> !getSelectedTreeNodeList().contains(fileNode))
                .collect(Collectors.toList());
        addAllToSelectedList(targetList);
    }

    @FXML
    protected void onTreeMenuRefreshAction(){
        buildTree();
    }

    @FXML
    public void onDirOnlyButtonAction(){
        MRTApp.configuration.setDirShowOnly(menuItemDirOnly.isSelected());
        updateUI();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * FXML EventHandler method
     * 已选文件列表
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    protected void onListMenuRemoveAction(){
        listViewSelectedFiles.getItems().removeAll(listViewSelectedFiles.getSelectionModel().getSelectedItems());

        refreshSelectedFileIndex();
    }

    @FXML
    protected void onListMenuRemoveAllAction(){
        listViewSelectedFiles.getItems().clear();

        refreshSelectedFileIndex();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * FXML EventHandler method
     * UI日志
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    protected void onUILoggerCopy(){
        String s = uiLogger.getSelectionModel().getSelectedItems().stream().toList().toString();

        try {
            Dialog<String> dialog = ClipboardDialog.getDialog(s);

            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * FXML EventHandler method
     * 规则定义面板
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    protected void onCreateNewFillingRule(ActionEvent actionEvent) {
        MRTRuleSelectorPaneController.showWindow(RuleType.FILLING);
    }

    @FXML
    protected void onDeleteFillingRule(ActionEvent actionEvent) {
        deleteRule(ruleFillingSetter);
    }

    @FXML
    protected void onMoveUpFillingRule(ActionEvent actionEvent) {
        moveUpRule(ruleFillingSetter);
    }

    @FXML
    protected void onMoveDownFillingRule(ActionEvent actionEvent) {
        moveDownRule(ruleFillingSetter);
    }

    @FXML
    protected void onClearFillingRule(ActionEvent actionEvent) {
        clearRule(ruleFillingSetter);
    }

    @FXML
    protected void onCreateNewReplaceRule(ActionEvent actionEvent) {
        MRTRuleSelectorPaneController.showWindow(RuleType.REPLACE);
    }

    @FXML
    protected void onDeleteReplaceRule(ActionEvent actionEvent) {
        deleteRule(ruleReplaceSetter);
    }

    @FXML
    protected void onMoveUpReplaceRule(ActionEvent actionEvent) {
        moveUpRule(ruleReplaceSetter);
    }

    @FXML
    protected void onMoveDownReplaceRule(ActionEvent actionEvent) {
        moveDownRule(ruleReplaceSetter);
    }

    @FXML
    protected void onClearReplaceRule(ActionEvent actionEvent) {
        clearRule(ruleReplaceSetter);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * Control Method
     * 规则定义面板
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void addRule(Rule rule){
        if (rule==null){ return; }
        if (rule.getType()!=null) {
            addRule(rule, -1);
        }else {
            throw new MRTRuleException(MRTRuleException.ErrorType.VER_UNDEFINED,
                    rule, "规则类型(RuleType)为空.");
        }
    }

    public void addRule(@NotNull Rule rule, int index){
        RuleDefiner definer = rule.createDefiner();
        if (definer!=null){
            addRule(rule, definer, index);
        }else {
            throw new MRTRuleException(MRTRuleException.ErrorType.VER_UNDEFINED,
                    rule, "规则定义器(RuleDefiner)为空.");
        }
    }

    void addRule(@NotNull Rule rule, RuleDefiner definer, int index){
        RuleType type = rule.getType();
        switch (type) {
            case FILLING -> addRule(ruleFillingSetter, definer, index);
            case REPLACE -> addRule(ruleReplaceSetter, definer, index);
        }
    }

    void addRule(@NotNull ListView<RuleDefiner> listView, RuleDefiner definer, int index){
        ObservableList<RuleDefiner> viewItems = listView.getItems();
        if (index!=-1){
            viewItems.add(index, definer);
        }else {
            viewItems.add(definer);
        }
    }

    void deleteRule(ListView<RuleDefiner> listView) {
        getSelectedItem(listView).ifPresent(
                ruleDefiner -> {
                    listView.getItems().remove(ruleDefiner);
                    resetRuleDefinerIndex(listView);
                }
        );
    }

    void moveUpRule(@NotNull ListView<RuleDefiner> listView) {
        int i = listView.getSelectionModel().getSelectedIndex();
        if (i!=0){
            Collections.swap(listView.getItems() , i , i-1);
            resetRuleDefinerIndex(listView);
        }
    }

    void moveDownRule(@NotNull ListView<RuleDefiner> listView) {
        int i = listView.getSelectionModel().getSelectedIndex();
        if (i!=listView.getItems().size()){
            Collections.swap(listView.getItems() , i , i+1);
            resetRuleDefinerIndex(listView);
        }
    }

    void resetRuleDefinerIndex(@NotNull ListView<RuleDefiner> listView){
        ObservableList<RuleDefiner> definers = listView.getItems();
        IntStream.range(0, definers.size()).forEachOrdered(i -> definers.get(i).setIndex(i));
    }

    void clearRule(@NotNull ListView<RuleDefiner> listView) {
        listView.getItems().clear();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * Control Method
     * 其他控件
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void changeStatusLabel(String text){
        Platform.runLater(() -> labelStatusLeft.setText(text));
    }

    public void statusLabelSetDefault(){
        Platform.runLater(() -> labelStatusLeft.setText("就绪"));
    }

    public void buildTree(){
        if (selectedPath !=null){
            fileTreeBuilder = new FileTreeBuilder(treeViewFileSelector, selectedPath);
            fileTreeBuilder.buildTree(selectedPath);
        }
    }

    public <T> Optional<T> getSelectedItem(@NotNull ListView<T> listView){
        return Optional.ofNullable(listView.getSelectionModel().getSelectedItem());
    }

    public <T> List<T> getSelectedItems(@NotNull ListView<T> listView){
        ObservableList<T> items = listView.getSelectionModel().getSelectedItems();
        return items != null ? new ArrayList<>(items) : new ArrayList<>();
    }

    public void updateUI(){
        this.setSelectedPath(new FileNode(new File(MRTApp.configuration.getDefaultPath())));
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
            selectedPath = new FileNode(item.getText());
            buildTree();
        });
        menuHistoryPath.getItems().add(0, item);
    }

    private void refreshSelectedFileIndex(){
        ObservableList<FileContainer> items = listViewSelectedFiles.getItems();
        IntStream.range(0, items.size())
                .forEachOrdered(i -> items.get(i).setIndex(i));
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

    public static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    public void printToUILogger(String text){
        Platform.runLater(() -> {
            uiLogger.getItems().add(formatter.format(new Date()) + " : " + text + "\n");
            int i = uiLogger.getItems().size();
            uiLogger.scrollTo(i);
            uiLogger.getSelectionModel().clearAndSelect(i-1);
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
            return "[" + index + "] " + node.toString();
        }


    }

}