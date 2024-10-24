package com.github.lunarconcerto.mrt.control;

import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

public class RuleDefiningListCell extends MFXListCell<RuleDefiningPane> {

    public static final DataFormat RULE_DEFINER_LIST_CELL_DATA_FORMAT = new DataFormat("RULE_DEFINER");

    protected final BooleanProperty indeterminate = new SimpleBooleanProperty(false);

    protected int indeterminateIndex = -1;

    public RuleDefiningListCell(MFXListView<RuleDefiningPane> listView, RuleDefiningPane data) {
        super(listView, data);
        init();
    }

    void init() {
        setPrefHeight(54.00);

        setOnDragDetected(this::onDragDetected);
        setOnDragDone(this::onDragDone);
        setOnDragDropped(this::onDragDropped);
        setOnDragEntered(this::onDragEntered);
        setOnDragExited(this::onDragExited);
        setOnDragOver(event -> event.acceptTransferModes(TransferMode.MOVE));
    }

    private void onDragEntered(@NotNull DragEvent event) {
        if (event.getTransferMode() == TransferMode.MOVE) {
            Object content = event.getDragboard().getContent(RULE_DEFINER_LIST_CELL_DATA_FORMAT);
            if (content == null) return;
            int index = (int) content;
            /* 预览状态 */
            setIndeterminate(true);
            indeterminateIndex = index;
            this.setVisible(true);
            getParentListView().swapItem(index, getIndex());
            setEffect(innerShadow());
        }
    }

    private void onDragDetected(@NotNull MouseEvent event) {
        Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.put(RULE_DEFINER_LIST_CELL_DATA_FORMAT, getIndex());
        dragboard.setDragView(this.snapshot(null, new WritableImage((int) this.getWidth(), (int) this.getHeight())),
                this.getWidth() / 2, 0);
        dragboard.setContent(content);

        this.setVisible(false);
        event.consume();
    }

    private void onDragDropped(DragEvent event) {
        setIndeterminate(false);
        indeterminateIndex = -1;
        setEffect(null);
    }

    private void onDragExited(DragEvent event) {
        /* 清除预览状态,回到原位 */
        if (isIndeterminate()) {
            setIndeterminate(false);
            getParentListView().swapItem(indeterminateIndex, getIndex());
            indeterminateIndex = -1;
            setEffect(null);
        }
    }

    private void onDragDone(DragEvent event) {
        this.setVisible(true);
    }

    protected InnerShadow innerShadow() {
        InnerShadow shadow = new InnerShadow();
        shadow.setOffsetX(1.0);
        shadow.setOffsetY(1.0);
        shadow.setColor(Color.LIGHTSKYBLUE);
        return shadow;
    }

    protected RuleDefiningListView getParentListView() {
        return (RuleDefiningListView) this.listView;
    }

    public boolean isIndeterminate() {
        return indeterminate.get();
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate.set(indeterminate);
    }

    public BooleanProperty indeterminateProperty() {
        return indeterminate;
    }
}
