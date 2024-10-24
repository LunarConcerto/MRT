package com.github.lunarconcerto.mrt.controller;

import com.github.lunarconcerto.mrt.MRTApplication;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.control.RuleDefiningListView;
import com.github.lunarconcerto.mrt.rule.io.RuleDefinerPreset;
import com.github.lunarconcerto.mrt.rule.io.SerializableRulePreset;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@NoArgsConstructor
public class PresetSelectorController extends Controller {

    @FXML
    protected ListView<SerializableRulePreset> presetListView;

    @FXML
    protected RuleDefiningListView ruleList;

    protected boolean load;

    @FXML
    protected void onCancel() {
        close();
    }

    @FXML
    protected void onConfirm() {
        if (load) {
            MRTApplication.mainController.loadFromPreset(presetListView.getSelectionModel().getSelectedItem());
        } else {
            SerializableRulePreset selectedItem = presetListView.getSelectionModel().getSelectedItem();
            ConfigurationManager.getManager().deletePreset(selectedItem);
            presetListView.getItems().remove(selectedItem);

            WindowManager.showInformation("删除成功", "删除了预设 : " + selectedItem.getPresetName());
        }

        close();
    }

    @FXML
    protected void onSelect(@NotNull MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            showPresetPreview(presetListView.getSelectionModel().getSelectedItem());
        }
    }

    @Override
    protected void init() {
        presetListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) showPresetPreview(newValue);
        });

        List<SerializableRulePreset> items = presetListView.getItems();
        items.addAll(ConfigurationManager.getManager().getPresetList());
        items.removeIf(item -> item.getPresetName().equals("default"));
        if (!items.isEmpty()) presetListView.getSelectionModel().selectFirst();
    }

    void showPresetPreview(@NotNull SerializableRulePreset preset) {
        RuleDefinerPreset definerPreset = preset.toRuleDefinerPreset();
        definerPreset.addToListView(ruleList);
    }

    public PresetSelectorController setLoad(boolean load) {
        this.load = load;
        return this;
    }
}
