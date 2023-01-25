package com.github.lunarconcerto.mrt.control;

import io.github.palexdev.materialfx.MFXResourcesLoader;
import io.github.palexdev.materialfx.controls.MFXTreeItem;
import io.github.palexdev.materialfx.controls.base.AbstractMFXTreeCell;
import io.github.palexdev.materialfx.controls.base.AbstractMFXTreeItem;
import io.github.palexdev.materialfx.skins.MFXTreeItemSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class DirectoryCheckTreeItem extends MFXTreeItem<File> {

    public static final DirectoryCheckTreeItem EMPTY = new DirectoryCheckTreeItem(
            new File(""), -1);

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 属性
     * * * * * * * * * * * * * * * * * * * * * * * * * */
    protected final BooleanProperty checked = new SimpleBooleanProperty(false);
    protected final IntegerProperty checkIndex = new SimpleIntegerProperty();
    protected final int level;
    private final String STYLE_CLASS = "mfx-check-tree-item";
    private final String STYLESHEET = MFXResourcesLoader.load("css/MFXTreeItem.css");

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 初始化 / 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected DirectoryCheckTreeItem(File data, int level) {
        super(data);
        this.level = level;
        initialize();
    }

    protected DirectoryCheckTreeItem(File data, Callback<AbstractMFXTreeItem<File>, AbstractMFXTreeCell<File>> cellFactory, int level) {
        super(data, cellFactory);
        this.level = level;
        initialize();
    }

    public static @NotNull DirectoryCheckTreeItem create(File data, int level) {
        return new DirectoryCheckTreeItem(data, level);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 静态方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public static @NotNull DirectoryCheckTreeItem create(File data, Callback<AbstractMFXTreeItem<File>, AbstractMFXTreeCell<File>> cellFactory, int layer) {
        return new DirectoryCheckTreeItem(data, cellFactory, layer);
    }

    protected void initialize() {
        setTranslateX(10);
        getStyleClass().add(STYLE_CLASS);

        getIndex();

        checkedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                getCheckedItems().add(this);
            } else {
                getCheckedItems().remove(this);
            }
        });
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void addChildItem(DirectoryCheckTreeItem item) {
        getItems().add(item);
    }

    public List<DirectoryCheckTreeItem> getCheckedItems() {
        return getSelectionModel().getCheckedItems();
    }

    public void checkAllItemsInNextLevel() {
        getChildItems().forEach(item -> item.setChecked(true));
    }

    public void reverseCheckedInNextLevel() {
        getChildItems().forEach(item -> item.setChecked(!item.isChecked()));
    }

    public List<DirectoryCheckTreeItem> getChildItems() {
        return getItems().stream()
                .map(item -> (DirectoryCheckTreeItem) item)
                .collect(Collectors.toList());
    }

    public boolean isParentOf(DirectoryCheckTreeItem item) {
        return getItems().contains(item);
    }

    public boolean isChildOf(DirectoryCheckTreeItem item) {
        return getItemParent().getItems().contains(item);
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 重写方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @Override
    public String getUserAgentStylesheet() {
        return STYLESHEET;
    }

    @Override
    protected void defaultCellFactory() {
        super.cellFactory.set(param -> new DirectoryCheckTreeCell((DirectoryCheckTreeItem) param));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DirectoryCheckTreeItemSkin(this);
    }

    @Override
    public DirectoryCheckTreeItem getItemParent() {
        return (DirectoryCheckTreeItem) super.getItemParent();
    }

    @Override
    public DirectoryCheckModel getSelectionModel() {
        return (DirectoryCheckModel) super.getSelectionModel();
    }

    public int getLevel() {
        return level;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * Setter / Getter
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public boolean isChecked() {
        return checked.get();
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

    public int getCheckIndex() {
        return checkIndex.get();
    }

    public void setCheckIndex(int checkIndex) {
        this.checkIndex.set(checkIndex);
    }

    public IntegerProperty checkIndexProperty() {
        return checkIndex;
    }

    public static class DirectoryCheckTreeItemSkin extends MFXTreeItemSkin<File> {

        public DirectoryCheckTreeItemSkin(MFXTreeItem<File> item) {
            super(item);
        }

        @Override
        protected AbstractMFXTreeCell<File> createCell() {
            return super.createCell();
        }
    }

}
