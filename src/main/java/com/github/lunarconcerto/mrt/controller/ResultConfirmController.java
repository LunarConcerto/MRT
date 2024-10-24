package com.github.lunarconcerto.mrt.controller;

import com.github.lunarconcerto.mrt.task.RenameResult;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.util.List;

public class ResultConfirmController extends Controller {

    @FXML
    public StackPane mainPane;

    @FXML
    public MFXTableView<RenameResult> resultTable;

    @FXML
    public Label showOldNameLabel;

    @FXML
    public MFXTextField editNewNameTextField;

    protected List<RenameResult> resultList;

    private RenameResult selected;

    public void initTable() {
        ObservableList<MFXTableColumn<RenameResult>> tableColumns = resultTable.getTableColumns();
        tableColumns.get(0).setRowCellFactory(renameResult -> new MFXTableRowCell<>(RenameResult::getTargetSourceName));
        tableColumns.get(1).setRowCellFactory(renameResult -> new MFXTableRowCell<>(RenameResult::getTargetNewName));
        resultTable.setItems(FXCollections.observableArrayList(resultList));

        resultTable.getSelectionModel().setAllowsMultipleSelection(false);
        resultTable.getSelectionModel().selectionProperty().addListener((MapChangeListener<Integer, RenameResult>) change -> {
            if (change.wasAdded()) {
                RenameResult added = change.getValueAdded();
                showOldNameLabel.setText(added.getTargetSourceName());
                editNewNameTextField.setText(added.getTargetNewName());
                selected = added;
            }
        });

        editNewNameTextField.setOnKeyPressed(event -> {
            if (selected != null && event.getCode() == KeyCode.ENTER) {
                selected.setTargetNewName(editNewNameTextField.getText());
                resultTable.update();
            }
        });

        MFXContextMenu.Builder.build(resultTable).addItem(
                MFXContextMenuItem.Builder.build()
                        .setText("删除")
                        .setOnAction(event -> {
                            List<RenameResult> removeResults = resultTable.getSelectionModel().getSelectedValues();
                            resultTable.getItems().removeAll(removeResults);
                            resultList.removeAll(removeResults);
                        })
                        .get()
        ).installAndGet();
    }

    public ResultConfirmController setResultList(List<RenameResult> resultList) {
        this.resultList = resultList;
        return this;
    }
}
