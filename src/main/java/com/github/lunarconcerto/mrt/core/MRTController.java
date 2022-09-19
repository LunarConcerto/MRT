package com.github.lunarconcerto.mrt.core;

import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.component.*;
import com.github.lunarconcerto.mrt.field.NameFieldManager;
import com.github.lunarconcerto.mrt.util.FileNode;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
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

/**
 * 主面板控制器
 */
@Data
@Log4j(topic = "controller")
public class MRTController {


    @FXML
    public ListView<AnchorPane> ruleSetterUp;

    /**
     * 当前选择的路径
     */
    protected FileNode selectingPath ;

    /**
     * 当前文件树建造者
     */
    protected FileTreeBuilder fileTreeBuilder ;

    /**
     * 多线程管理器
     */
    protected WorkerManager workerManager ;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 控件
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    // GUI模拟控制台(仅可打印消息)
    @FXML
    protected ListView<String> uiLogger;

    // [选择文件夹]按钮
    @FXML
    protected MenuItem menuItemSelectFile;

    // [运行]按钮
    @FXML
    protected Button button_start;

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

    // 右侧用于设置命名规则的面板
    @FXML
    protected ScrollPane name_field_pane;

    /*
    * 文件选择列表右键菜单的元素
    * */

    // 文件树视图中右键选择的[仅显示文件夹]菜单按钮
    @FXML
    protected CheckMenuItem menuItemDirOnly;

    /*
    * [设置]页面中的元素
    * */

    @FXML
    protected TextField input_proxy_host;

    @FXML
    protected TextField input_proxy_port;

    @FXML
    protected TextField input_default_file_path;

    @FXML
    protected Button button_select_default_path;

    @FXML
    protected CheckBox enable_proxy;

    @FXML
    protected CheckBox enable_preview;

    @FXML
    protected ListView<AnchorPane> other_setting_list;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public MRTController() {
        workerManager = new WorkerManager() ;
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
    public void onSelectFilePathButtonAction(){
        File choosingFile = selectingPath != null ? createNewDirectoryChooser(selectingPath) : createNewDirectoryChooser();

        if (choosingFile==null) return;

        selectingPath = new FileNode(choosingFile);
        log.debug("选择的文件路径为:" + selectingPath.getFullName() );
        buildTree();
    }

    @FXML
    public void onStartButtonAction(){
/*        List<FileNode> fileNodeList = selected_file_list.getItems().stream().toList();
        Rule rule = combe_box_rename_rule.getSelectionModel().getSelectedItem();

        if (fileNodeList.isEmpty()){
            printToUIConsole("未选择要进行处理的文件夹/文件");
            return;
        }

        if (rule instanceof EmptyRule){
            printToUIConsole("未选择要使用的重命名规则");
            return;
        }

        workerManager.startWork(() -> {
            Platform.runLater(()->changeStatusLabel("运行中"));

            List<RenameResult> result = rule.run(fileNodeList , progress_bar);

            Platform.runLater(() -> progress_bar.setProgress(0));

            RenameProgress progress = new RenameProgress(result);
            progress.run(progress_bar);
            Platform.runLater(this::reset);
        });*/
    }


    /*
    * 文件树选择菜单触发
    * */

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

    /*
    * 已选文件列表菜单触发
    * */

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

    /*
    * 命名设置菜单触发
    * */

    @FXML
    public void onAddNewNameField(ActionEvent actionEvent) {
        NameFieldManager.getInstance().addEmptyPane();
    }

    @FXML
    public void onClearNameField(ActionEvent actionEvent){
        NameFieldManager.getInstance().clearAllPane();
    }

    /*
    * UI控制台菜单触发
    * */

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


    /*
    * 设置面板部分触发
    * */

    @FXML
    public void onSelectionTabChange() {
        collectSetting();

        ConfigurationManager.getManager().save();
    }

    @FXML
    public void onSelectDefaultPathButtonAction(){
        File choosingFile = createNewDirectoryChooser();
        if (choosingFile == null){
            return;
        }

        MRTApp.configuration.setDefaultPath(choosingFile.getPath());
        ConfigurationManager.getManager().save();
    }

    public void onChangeProxyStatus() {
        boolean enableProxy = enable_proxy.isSelected();
        if (enableProxy){
            MRTApp.configuration.setEnableProxy(true).enableProxy();
        }else {
            MRTApp.configuration.setEnableProxy(false).disableProxy();
        }

        collectSetting();
        updateProxyInputField();

        ConfigurationManager.getManager().save();
    }

    @FXML
    public void onProxyHostInput(){
        MRTApp.configuration.setProxyHost(input_proxy_host.getText());
    }

    @FXML
    public void onProxyPortInput(){
        MRTApp.configuration.setProxyPort(input_proxy_port.getText());
    }


    /**
     * 该方法用于主动保存某些保存不太即时的设置项
     */
    public void collectSetting(){
//        Configuration configuration = MRTApp.configuration;
//        configuration.setProxyHost(this.input_proxy_host.getText());
//        configuration.setProxyPort(this.input_proxy_port.getText());
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 部分有关控件的操作
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void buildTree(){
        if (selectingPath!=null){
            workerManager.startWork(() -> {
                fileTreeBuilder = new FileTreeBuilder(treeViewFileSelector, selectingPath);
                fileTreeBuilder.buildTree(selectingPath);
            });
        }
    }

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
//        this.getInput_proxy_host().setText(MRTApp.configuration.getProxyHost());
//        this.getInput_proxy_port().setText(MRTApp.configuration.getProxyPort());
//
//        this.getInput_default_file_path().setText(MRTApp.configuration.getDefaultPath());
        this.setSelectingPath(new FileNode(new File(MRTApp.configuration.getDefaultPath())));
//
//        if (MRTApp.configuration.isDirShowOnly()) this.getDir_only().setSelected(true);
//        this.updateProxyInputField();

        this.buildTree();

    }

    public void updateProxyInputField(){
        boolean enableProxy = MRTApp.configuration.isEnableProxy();
        if (enableProxy){
            this.input_proxy_host.setEditable(true);
            this.input_proxy_port.setEditable(true);

            this.input_proxy_host.setDisable(false);
            this.input_proxy_port.setDisable(false);

            this.enable_proxy.setSelected(true);
        }else {
            this.input_proxy_host.setEditable(false);
            this.input_proxy_port.setEditable(false);

            this.input_proxy_host.setDisable(true);
            this.input_proxy_port.setDisable(true);

            this.enable_proxy.setSelected(false);
        }
    }

    public void reset(){
        statusLabelSetDefault();
        listViewSelectedFiles.getItems().clear();
        progressBar.setProgress(0);

        buildTree();
    }

    private File createNewDirectoryChooser(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择一个文件夹");
        return chooser.showDialog(new Stage());
    }

    private File createNewDirectoryChooser(FileNode file){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("选择一个文件夹");
        chooser.setInitialDirectory(file);
        return chooser.showDialog(new Stage());
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