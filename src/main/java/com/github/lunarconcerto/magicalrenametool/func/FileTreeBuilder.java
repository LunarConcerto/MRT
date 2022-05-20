package com.github.lunarconcerto.magicalrenametool.func;

import com.github.lunarconcerto.magicalrenametool.core.RenameToolApplication;
import com.github.lunarconcerto.magicalrenametool.util.FileNode;
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

    protected FileNode file ;

    public static int layerLength = 1 ;

    protected int fileSize = 1 ;

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
        fileSize = 1 ;
        file.setLevel(0);
        TreeItem<FileNode> root = new TreeItem<>(file);
        root.setGraphic(getFolderIconNode());

        treeView.setRoot(root);

        buildChild(root);
        RenameToolApplication.printToUIConsole("读取了%s个文件".formatted(fileSize));

        root.setExpanded(true);
    }

    private void buildChild(@NotNull TreeItem<FileNode> root){

        for (FileNode fileNode : Objects.requireNonNull(root.getValue().listFiles())) {
            if (fileNode != null) {
                if (RenameToolApplication.configuration.isDirShowOnly() && !fileNode.isDirectory()) {
                    continue;
                }

                TreeItem<FileNode> item = new TreeItem<>(fileNode);
                root.getChildren().add(item);
                item.getValue().setLevel(root.getValue().getLevel() + 1);

                ifContinueAndSetIcon(item, fileNode);
                fileSize++;
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
        if (fileSize>2000){
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
