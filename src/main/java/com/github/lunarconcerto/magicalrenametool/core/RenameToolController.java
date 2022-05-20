package com.github.lunarconcerto.magicalrenametool.core;

import com.github.lunarconcerto.magicalrenametool.config.ConfigurationLoader;
import com.github.lunarconcerto.magicalrenametool.func.*;
import com.github.lunarconcerto.magicalrenametool.rule.EmptyRule;
import com.github.lunarconcerto.magicalrenametool.rule.RenameResult;
import com.github.lunarconcerto.magicalrenametool.rule.Rule;
import com.github.lunarconcerto.magicalrenametool.util.FileNode;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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

@Data
@Log4j(topic = "controller")
public class RenameToolController {

    protected FileNode selectingPath ;

    protected FileTreeBuilder fileTreeBuilder ;

    protected WorkerManager workerManager ;

    /*
    * main elements
    * */

    @FXML
    protected ListView<String> console_list;

    @FXML
    protected Button button_select_file;

    @FXML
    protected Button button_start;

    @FXML
    protected TreeView<FileNode> file_tree;

    @FXML
    protected ListView<FileNode> selected_file_list;

    @FXML
    protected Label label_status ;

    @FXML
    protected ProgressBar progress_bar;

    /*
    * Settings elements
    * */

    @FXML
    protected TextField input_proxy_host;

    @FXML
    protected TextField input_proxy_post;

    @FXML
    protected TextField input_default_file_path;

    @FXML
    protected CheckMenuItem dir_only;

    @FXML
    protected Button button_select_default_path;

    @FXML
    protected ComboBox<Rule> combe_box_rename_rule;

    @FXML
    protected ScrollPane rule_pane;

    @FXML
    protected ImageView image_cafe;

    public RenameToolController() {
        workerManager = new WorkerManager() ;
    }

    /*
    * 运行面板触发
    * */

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
        List<FileNode> fileNodeList = selected_file_list.getItems().stream().toList();
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
        });
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
        file_tree.getSelectionModel().selectAll();
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
        RenameToolApplication.configuration.setDirShowOnlyAndUpdateUI(dir_only.isSelected());
    }

    /*
    * 已选文件列表菜单触发
    * */

    @FXML
    public void onListMenuRemoveAction(){
        selected_file_list.getItems().removeAll(selected_file_list.getSelectionModel().getSelectedItems());

        selected_file_list.refresh();
    }

    @FXML
    public void onListMenuRemoveAllAction(){
        selected_file_list.getItems().clear();

        selected_file_list.refresh();
    }

    /*
    * UI控制台菜单触发
    * */

    @FXML
    public void onUIConsoleCopy(){
        String s = console_list.getSelectionModel().getSelectedItems().stream().toList().toString();

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
    public void onSelectDefaultPathButtonAction(){
        File choosingFile = createNewDirectoryChooser();
        if (choosingFile==null){
            return;
        }

        RenameToolApplication.configuration.setDefaultPath(choosingFile);
        ConfigurationLoader.getLoader().save(RenameToolApplication.configuration);
    }

    @FXML
    public void onRuleSelectAction(){
        Rule rule = combe_box_rename_rule.getSelectionModel().getSelectedItem();
        if (rule!=null){
            saveSelectedRule(rule);
            setSelectedRule(rule);
        }
    }

    @FXML
    public void onProxyHostInput(){
        RenameToolApplication.configuration.setProxyHost(input_proxy_host.getText());
    }

    @FXML
    public void onProxyPortInput(){
        RenameToolApplication.configuration.setProxyPort(input_proxy_post.getText());
    }

    /*
    * 关于页面触发
    * */

    @FXML
    public void onLink1(){
        String url = "https://github.com/LunarConcerto/AutoRenameToolForDoujinOnsei";

        try {
            Dialog<String> dialog = ClipboardDialog.getDialog(url);

            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLink2(){
        String url = "https://github.com/LunarConcerto/MRT";

        try {
            Dialog<String> dialog = ClipboardDialog.getDialog(url);

            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onContributeButtonAction(){
        try {
            Dialog<String> dialog = ContributeDialog.getDialog();

            dialog.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * 部分UI控制
    * */

    public void buildTree(){
        if (selectingPath!=null){
            workerManager.startWork(() -> {
                fileTreeBuilder = new FileTreeBuilder(file_tree, selectingPath);
                fileTreeBuilder.buildTree(selectingPath);
            });
        }
    }

    public void changeStatusLabel(String text){
        label_status.setText(text);
    }

    public void statusLabelSetDefault(){
        label_status.setText("就绪");
    }

    /*
    * 其他方法
    * */

    public void reset(){
        statusLabelSetDefault();
        selected_file_list.getItems().clear();
        progress_bar.setProgress(0);
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
        ObservableList<FileNode> listItems = selected_file_list.getItems();
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
        printToUIConsole("将%s个文件加入到待处理列表".formatted(addedItems));
    }

    private boolean isChild(@NotNull TreeItem<FileNode> parent , @NotNull TreeItem<FileNode> child){
        if (parent.getValue().getLevel()==child.getValue().getLevel()-1){
            return parent.getChildren().contains(child) ;
        }
        return false ;
    }

    private @NotNull List<FileNode> getRootChildrenNodeList(){
        return treeItemListToFileNodeList(file_tree.getRoot().getChildren()) ;
    }

    private @NotNull List<FileNode> getSelectedTreeNodeList(){
        List<TreeItem<FileNode>> treeItemList =
                file_tree.getSelectionModel()
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
        return file_tree
                .getSelectionModel()
                .getSelectedItems()
                .stream()
                .toList();
    }

    public void addRule(Rule rule){
        this.combe_box_rename_rule.getItems().add(rule);
    }

    public void setSelectedRule(@NotNull Rule rule){
        try {
            AnchorPane panel = rule.getSettingPanel();
            rule_pane.setContent(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSelectedRule(@NotNull Rule rule){
        RenameToolApplication.configuration.setSelectedRule(rule.getClass().getName());
    }

    public static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    public void printToUIConsole(String text){
        Platform.runLater(() -> {
            console_list.getItems().add(formatter.format(new Date()) + " : " + text + "\n");
            console_list.scrollTo(console_list.getItems().size());
        });

    }


}