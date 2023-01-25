package com.github.lunarconcerto.mrt.control;

import animatefx.animation.ZoomIn;
import animatefx.animation.ZoomOut;
import io.github.palexdev.materialfx.MFXResourcesLoader;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.cell.MFXSimpleTreeCell;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DirectoryCheckTreeCell extends MFXSimpleTreeCell<File> {

    private static final PseudoClass CHECKED_PSEUDO_CLASS = PseudoClass.getPseudoClass("checked");
    protected final BooleanProperty checked = new SimpleBooleanProperty(false);
    protected final MFXCheckbox checkbox;
    private final String STYLE_CLASS = "mfx-check-tree-cell";
    private final String STYLESHEET = MFXResourcesLoader.load("css/MFXCheckTreeCell.css");
    protected IndexIcon indexIcon;

    protected MFXFontIcon icon;


    protected DirectoryCheckTreeCell(DirectoryCheckTreeItem item) {
        super(item);
        checkbox = new MFXCheckbox("");
        initialize(item);
    }

    protected DirectoryCheckTreeCell(DirectoryCheckTreeItem item, double fixedHeight) {
        super(item, fixedHeight);
        checkbox = new MFXCheckbox("");
        initialize(item);
    }

    @Contract("_ -> new")
    public static @NotNull DirectoryCheckTreeCell create(DirectoryCheckTreeItem item) {
        return new DirectoryCheckTreeCell(item);
    }

    private void initialize(@NotNull DirectoryCheckTreeItem item) {
        getChildren().add(1, checkbox);

        getStyleClass().add(STYLE_CLASS);
        setFixedCellSize(32);

        checked.bind(item.checkedProperty());

        icon = item.getData().isDirectory() ?
                new MFXFontIcon("mfx-folder") :
                new MFXFontIcon("mfx-file");
        icon.setSize(30);
        getChildren().add(2, icon);

        indexIcon = defaultIndexIcon(String.valueOf(item.getCheckIndex()));

        addListener(item);
    }

    protected void addListener(@NotNull DirectoryCheckTreeItem item) {
        checked.addListener(invalidate -> pseudoClassStateChanged(CHECKED_PSEUDO_CLASS, checked.get()));
        checked.addListener((observable, oldValue, newValue) -> checkbox.setSelected(newValue));

        getCheckbox().selectedProperty().addListener((observable, oldValue, newValue) -> item.setChecked(newValue));

        item.checkIndexProperty().addListener((observable, oldValue, newValue) -> indexIcon.getLabel().setText(newValue.toString()));
        item.checkedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                indexIcon.setVisible(true);
                getChildren().add(0, indexIcon.getNode());
                new ZoomIn(indexIcon.getNode())
                        .setSpeed(5)
                        .play();
            } else {
                ZoomOut zoom = new ZoomOut(indexIcon.getNode());
                zoom.setOnFinished(event -> {
                    indexIcon.setVisible(false);
                    getChildren().remove(indexIcon.getNode());
                });
                zoom.setSpeed(5).play();
            }
        });
    }

    protected IndexIcon defaultIndexIcon(String index) {
        return new IndexIcon.CircleIndexIcon(index);
    }

    @Override
    protected void render(@NotNull File data) {
        Label label = new Label(data.getName());
        label.getStyleClass().add("data-label");
        getChildren().add(label);
    }

    @Override
    public String getUserAgentStylesheet() {
        return STYLESHEET;
    }

    public MFXCheckbox getCheckbox() {
        return checkbox;
    }

    public boolean isChecked() {
        return checked.get();
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

}
