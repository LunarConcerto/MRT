package com.github.lunarconcerto.mrt.component;

import com.github.lunarconcerto.mrt.core.MRTApp;
import com.github.lunarconcerto.mrt.util.FileNode;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.paint.Paint;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

@Data
@Log4j(topic = "FileTreeBuilder")
public class FileTreeBuilder {

    protected final TreeView<FileNode> treeView;

    /**
     * 当前文件树所显示的目录的根级
     */
    protected FileNode file ;

    /**
     * 文件树最大子层级
     */
    public static int layerLength = 1 ,

    /**
     * 文件树最大文件数限制
     */
    fileSizeLimit = 2000 ;

    /**
     * 当前文件树已显示的文件规模
     */
    protected int loadedFileAmount = 1 ;

    public FileTreeBuilder(TreeView<FileNode> treeView, @NotNull FileNode file) {
        this.treeView = treeView;
        this.file = file;
        file.setRoot();
    }

    public FileTreeBuilder(TreeView<FileNode> treeView) {
        this.treeView = treeView;
    }

    public TreeView<FileNode> buildTree(){
        if (file==null){
            return null ;
        }
        build();

        return treeView ;
    }

    public TreeView<FileNode> buildTree(FileNode file){
        this.file = file ;
        Platform.runLater(this::build);
        return treeView ;
    }

    private void build(){
        loadedFileAmount = 1 ;
        file.setLevel(0);
        TreeItem<FileNode> root = new TreeItem<>(file);
        root.setGraphic(getFolderIconNode());

        treeView.setRoot(root);

        buildChild(root);
        MRTApp.printToUIConsole("读取了%s个文件".formatted(loadedFileAmount));

        root.setExpanded(true);
    }

    private void buildChild(@NotNull TreeItem<FileNode> root){

        for (FileNode fileNode : Objects.requireNonNull(root.getValue().listFiles())) {
            if (fileNode != null) {
                if (MRTApp.configuration.isDirShowOnly() && !fileNode.isDirectory()) {
                    continue;
                }

                TreeItem<FileNode> item = new TreeItem<>(fileNode);
                root.getChildren().add(item);
                item.getValue().setLevel(root.getValue().getLevel() + 1);

                ifContinueAndSetIcon(item, fileNode);
                loadedFileAmount++;
                Platform.runLater(() -> {
                    MRTApp.controller.getProgressBar().setProgress( (double) fileSizeLimit / loadedFileAmount);
                });

                ifPause();
            }
        }
    }

    private void ifContinueAndSetIcon(TreeItem<FileNode> item , @NotNull FileNode file){
        if (!file.isDirectory()){
            item.setGraphic(getFileIconNode());
        }else{
            item.setGraphic(getFolderIconNode());
            if (file.getLevel()<=layerLength){
                buildChild(item);
            }
        }

    }

    private void ifPause(){
        if (loadedFileAmount > fileSizeLimit){
            treeView.setRoot(null);
            throw new RuntimeException("文件规模过大,请重新选择文件夹。");
        }
    }

    public void setFile(@NotNull FileNode file) {
        this.file = file;
        file.setRoot();
    }

    public FontIcon getFolderIconNode(){
        FontIcon icon = new FontIcon("fa-folder-o");
        icon.setIconColor(Paint.valueOf("black"));
        return icon ;
    }

    public FontIcon getFileIconNode(){
        FontIcon icon = new FontIcon("fa-file-o");
        icon.setIconColor(Paint.valueOf("black"));
        return icon ;
    }
}
