package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.component.RenameResult;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MRTResultConfirmPaneController extends AnchorPane {

    protected List<RenameResult> resultList ;

    protected Dialog<List<RenameResult>> resultDialog ;

    @FXML
    protected TableView<RenameResult> resultTable ;

    @FXML
    protected TableColumn<RenameResult, String> sourceNameColumn , newNameColumn ;

    public MRTResultConfirmPaneController(List<RenameResult> resultList) {
        this.resultList = resultList;
    }

    void init() {
        sourceNameColumn.setCellValueFactory(new PropertyValueFactory<>("targetSourceName"));
        newNameColumn.setCellValueFactory(new PropertyValueFactory<>("targetNewName"));
        newNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        newNameColumn.setOnEditCommit(event -> event.getRowValue().setTargetNewName(event.getNewValue()));

        resultTable.setItems(FXCollections.observableArrayList(resultList));
    }

    public static @NotNull Dialog<List<RenameResult>> getDialog(List<RenameResult> resultList){
        MRTResultConfirmPaneController controller = getController(resultList);

        Dialog<List<RenameResult>> dialog = new Dialog<>();
        dialog.setTitle("确认当前生成的结果");
        dialog.getDialogPane().setContent(controller);
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(event -> dialog.close());

        ButtonType confirmType = new ButtonType("确认并继续 >>>", ButtonBar.ButtonData.RIGHT);
        ButtonType cancelType = new ButtonType("<<< 清空并返回", ButtonBar.ButtonData.LEFT);

        dialog.getDialogPane().getButtonTypes().add(0, cancelType);
        dialog.getDialogPane().getButtonTypes().add(1, confirmType);

        dialog.setResultConverter(param -> param.getButtonData() == ButtonBar.ButtonData.RIGHT
                ? dialog.getResult() : new ArrayList<>());

        controller.setResultDialog(dialog);

        return dialog ;
    }

    private static @NotNull MRTResultConfirmPaneController getController(List<RenameResult> resultList){
        try {
            MRTResultConfirmPaneController controller = new MRTResultConfirmPaneController(resultList);

            ControllerUtil.loadWindow(controller, "mrt.result.confirm.fxml");
            controller.init();

            return controller ;
        } catch (Exception e) {
            throw new MRTRuntimeException(e);
        }
    }

    private void setResultDialog(@NotNull Dialog<List<RenameResult>> resultDialog) {
        this.resultDialog = resultDialog;

        resultDialog.setResult(resultList);
    }
}
