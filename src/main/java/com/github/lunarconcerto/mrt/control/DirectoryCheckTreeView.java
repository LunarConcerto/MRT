package com.github.lunarconcerto.mrt.control;

import com.github.lunarconcerto.mrt.util.PopOvers;
import com.github.lunarconcerto.mrt.util.Texts;
import com.github.lunarconcerto.mrt.util.Worker;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.base.AbstractMFXTreeItem;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.input.TransferMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class DirectoryCheckTreeView extends MFXTreeView<File> {

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 属性
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected final IntegerProperty depth = new SimpleIntegerProperty(2);

    protected final ObjectProperty<File> targetDirectory = new SimpleObjectProperty<>(null);

    protected final BooleanProperty dirShowOnly = new SimpleBooleanProperty(true);

    protected EventHandler<DirectoryUpdateEvent> onOpenDirectory = event -> {
    };

    protected EventHandler<DirectoryUpdateEvent> onOpenDirectoryComplete = event -> {
    };

    protected MFXContextMenu contextMenu;

    protected int loadedFileCount = 1;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 初始化 / 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public DirectoryCheckTreeView() {
        init();
    }

    public DirectoryCheckTreeView(MFXCheckTreeItem<File> root) {
        super(root);
        init();
    }

    protected void init() {
        defaultContextMenu();

        dirShowOnly.addListener((observable, oldValue, newValue) -> {
            reload();
        });
        targetDirectory.addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.exists()) scanDirectory(newValue);
        });

        defaultDragAction();
    }

    protected void defaultDragAction() {
        setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });
        setOnDragEntered(event -> {
            setOpacity(0.5);
            PopOvers.showPopOver(this, "将文件夹拖放到此处,可打开该目录.");
        });
        setOnDragExited(event -> setOpacity(1));
        setOnDragDropped(event -> {
            setOpacity(1);
            if (!event.getDragboard().hasFiles()) return;
            File file = event.getDragboard().getFiles().get(0);
            if (file.isDirectory()) {
                setTargetDirectory(file);
                event.consume();
            }
        });
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public List<DirectoryCheckTreeItem> getCheckedItems() {
        return ((DirectoryCheckTreeItem) getRoot()).getCheckedItems();
    }

    public List<File> getCheckedFiles() {
        return getCheckedItems().stream().map(AbstractMFXTreeItem::getData).collect(Collectors.toList());
    }

    protected void scanDirectory(@NotNull File file) {
        clear();
        Worker.startWork(() -> {
            onOpenDirectory.handle(new DirectoryUpdateEvent(file, 1));
            DirectoryCheckTreeItem rootNode = buildRootNode(file);
            Platform.runLater(() -> {
                setRoot(rootNode);
                onOpenDirectoryComplete.handle(new DirectoryUpdateEvent(file, loadedFileCount));
            });
        });
    }

    public void clear() {
        loadedFileCount = 1;
        setRoot(DirectoryCheckTreeItem.EMPTY);
        getCheckedItems().clear();
    }

    public void reload() {
        if (getRoot() != null) getCheckedItems().clear();
        if (getTargetDirectory() != null && getTargetDirectory().exists()) scanDirectory(getTargetDirectory());
    }

    protected DirectoryCheckTreeItem buildRootNode(final File file) {
        DirectoryCheckTreeItem root = DirectoryCheckTreeItem.create(file, 0);
        buildChildNode(root);
        return root;
    }

    private void buildChildNode(@NotNull DirectoryCheckTreeItem root) {
        File[] files = root.getData().listFiles();
        if (files == null) return;

        for (File file : files) {
            boolean isDirectory = file.isDirectory();
            if (isDirShowOnly() && !isDirectory) continue;

            DirectoryCheckTreeItem child = DirectoryCheckTreeItem.create(file, root.getLevel() + 1);
            root.addChildItem(child);
            loadedFileCount++;

            if (isDirectory && child.getLevel() < depthProperty().get()) buildChildNode(child);
        }
    }

    private void defaultContextMenu() {
        MFXContextMenuItem selectButton = MFXContextMenuItem.Builder.build()
                .setText("选择")
                .setIcon(Texts.FILE_SELECT)
                .setOnAction(e -> {
                    List<DirectoryCheckTreeItem> items = getSelectedItems();
                    if (items != null && !items.isEmpty()) items.forEach(item -> item.setChecked(true));
                })
                .get();
        MFXTooltip.of(selectButton, "勾选选中的项目.")
                .install();

        MFXContextMenuItem selectAllButton = MFXContextMenuItem.Builder.build()
                .setText("全选")
                .setIcon(Texts.FILE_SELECT_ALL)
                .setOnAction(e -> {
                    DirectoryCheckTreeItem selectionItem = getSelectedItem();
                    if (selectionItem != null) {
                        selectionItem.getItemParent().checkAllItemsInNextLevel();
                    }
                })
                .get();
        MFXTooltip.of(selectAllButton, "勾选该项目的所有同层级项目.")
                .install();

        MFXContextMenuItem reverseSelectButton = MFXContextMenuItem.Builder.build()
                .setText("反选")
                .setIcon(Texts.FILE_SELECT_INVERSE)
                .setOnAction(event -> {
                    DirectoryCheckTreeItem selectionItem = getSelectedItem();
                    if (selectionItem != null) {
                        selectionItem.getItemParent().reverseCheckedInNextLevel();
                    }
                })
                .get();
        MFXTooltip.of(reverseSelectButton, "反转所有当前层级项目的勾选状态.")
                .install();

        MFXContextMenuItem refreshButton = MFXContextMenuItem.Builder.build()
                .setText("刷新")
                .setIcon(Texts.FILE_REFRESH)
                .setOnAction(event -> {
                    reload();
                })
                .get();
        MFXTooltip.of(refreshButton, "重新读取当前目录.")
                .install();

        MFXContextMenu.Builder builder = MFXContextMenu.Builder.build(this);
        builder.addItems(
                selectButton,
                selectAllButton,
                reverseSelectButton,
                refreshButton
        );
        contextMenu = builder.installAndGet();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 重写方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    protected void installSelectionModel() {
        this.setSelectionModel(new DirectoryCheckModel());
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * Setter / Getter
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public DirectoryCheckModel getCheckModel() {
        return (DirectoryCheckModel) getSelectionModel();
    }

    public boolean isDirShowOnly() {
        return dirShowOnly.get();
    }

    public void setDirShowOnly(boolean dirShowOnly) {
        this.dirShowOnly.set(dirShowOnly);
    }

    public BooleanProperty dirShowOnlyProperty() {
        return dirShowOnly;
    }

    public File getTargetDirectory() {
        return targetDirectory.get();
    }

    public void setTargetDirectory(File targetDirectory) {
        this.targetDirectory.set(targetDirectory);
    }

    public ObjectProperty<File> targetDirectoryProperty() {
        return targetDirectory;
    }

    public DirectoryCheckTreeItem getSelectedItem() {
        return ((DirectoryCheckTreeItem) getSelectionModel().getSelectedItem());
    }

    public List<DirectoryCheckTreeItem> getSelectedItems() {
        return ((DirectoryCheckModel) getSelectionModel()).getSelectedItemList();
    }

    public DirectoryCheckTreeView setOnOpenDirectory(EventHandler<DirectoryUpdateEvent> onOpenDirectory) {
        this.onOpenDirectory = onOpenDirectory;
        return this;
    }

    public DirectoryCheckTreeView setOnOpenDirectoryComplete(EventHandler<DirectoryUpdateEvent> onOpenDirectoryComplete) {
        this.onOpenDirectoryComplete = onOpenDirectoryComplete;
        return this;
    }

    public int getDepth() {
        return depth.get();
    }

    public void setDepth(int depth) {
        this.depth.set(depth);
    }

    public IntegerProperty depthProperty() {
        return depth;
    }

    public interface EventHandler<T> {

        void handle(T event);

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectoryUpdateEvent {

        protected File targetFile;

        protected int loadedFileCount;

    }

}
