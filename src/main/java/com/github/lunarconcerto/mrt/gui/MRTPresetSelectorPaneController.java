package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.RuleDefinerPreset;
import com.github.lunarconcerto.mrt.rule.SerializableRulePreset;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class MRTPresetSelectorPaneController extends AnchorPane {

    static boolean exists ;

    @FXML
    protected ListView<SerializableRulePreset> presetListView ;

    @FXML
    protected ListView<RuleDefiner> previewFillingRule , previewReplaceRule ;

    protected Stage stage ;

    public MRTPresetSelectorPaneController() {
    }

    @FXML
    protected void onCancel(){
        exists = false ;
        stage.close();
    }

    @FXML
    protected void onConfirm(){
        exists = false ;
        stage.close();
    }

    void init() {
        stage.setOnCloseRequest(e -> {exists = false;});

        if (!presetListView.getItems().isEmpty()){
            presetListView.getSelectionModel().select(0);
        }
    }

    public MRTPresetSelectorPaneController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public static void showWindow(){
        if (exists) return;
        try {
            MRTPresetSelectorPaneController controller = new MRTPresetSelectorPaneController();

            Parent parent = ControllerUtil.loadWindow(controller, "mrt.preset.selector.fxml");
            Stage stage = ControllerUtil.newStage(parent, "选择预设", 600, 600);

            controller.setStage(stage).init();

            exists = true ;
            stage.show();
        }catch (Exception e){
            throw new MRTRuntimeException(e);
        }
    }

}
