package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.RuleDefinerPreset;
import com.github.lunarconcerto.mrt.rule.SerializableRulePreset;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class MRTPresetSelectorPaneController extends AnchorPane {

    static boolean exists ;

    @FXML
    protected ListView<SerializableRulePreset> presetListView ;

    @FXML
    protected ListView<RuleDefiner> previewFillingRule , previewReplaceRule ;

    @SuppressWarnings("ClassEscapesDefinedScope")
    protected Type type ;

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
        switch (type){
            case LOAD -> MRTApp.mainController.loadFromPreset(presetListView.getSelectionModel().getSelectedItem());
            case DELETE -> ConfigurationManager.getManager().deletePreset(presetListView.getSelectionModel().getSelectedItem());
        }


        exists = false ;
        stage.close();
    }

    @FXML
    protected void onSelect(@NotNull MouseEvent event){
        if (event.getButton() == MouseButton.PRIMARY) {
            showPresetPreview(presetListView.getSelectionModel().getSelectedItem());
        }
    }

    void showPresetPreview(@NotNull SerializableRulePreset preset){
        RuleDefinerPreset definerPreset = preset.toRuleDefinerPreset();
        definerPreset.addToFillingListView(previewFillingRule);
        definerPreset.addToReplaceListView(previewReplaceRule);
    }

    void init() {
        stage.setOnCloseRequest(e -> exists = false);

        presetListView.getItems().addAll(ConfigurationManager.getManager().getPresetList());

        if (!presetListView.getItems().isEmpty()){
            presetListView.getSelectionModel().select(0);
            showPresetPreview(presetListView.getItems().get(0));
        }
    }

    public MRTPresetSelectorPaneController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public MRTPresetSelectorPaneController setType(Type type) {
        this.type = type;
        return this;
    }

    public static void showDeleteWindow(){
        showWindow(Type.DELETE);
    }

    public static void showLoadWindow(){
        showWindow(Type.LOAD);
    }

    private static void showWindow(Type type){
        if (exists) return;
        try {
            MRTPresetSelectorPaneController controller = new MRTPresetSelectorPaneController();

            Parent parent = ControllerUtil.loadWindow(controller, "mrt.preset.selector.fxml");
            Stage stage = ControllerUtil.newStage(parent, "选择预设", 600, 600);

            controller.setType(type)
                    .setStage(stage)
                    .init();

            exists = true ;
            stage.show();
        }catch (Exception e){
            throw new MRTRuntimeException(e);
        }
    }

    enum Type{

        LOAD,
        DELETE

    }

}
