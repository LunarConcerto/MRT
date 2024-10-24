package com.github.lunarconcerto.mrt.rule.io;

import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import io.github.palexdev.materialfx.controls.MFXListView;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class RuleDefinerPreset {

    protected List<RuleDefiningPane> ruleDefiningPaneList;

    public RuleDefinerPreset() {
        ruleDefiningPaneList = new ArrayList<>();
    }

    public void addToListView(@NotNull MFXListView<RuleDefiningPane> list) {
        addToListView(list, ruleDefiningPaneList);
    }

    void addToListView(@NotNull MFXListView<RuleDefiningPane> list, @NotNull List<RuleDefiningPane> definerList) {
        list.getItems().clear();
        definerList.forEach(definer -> list.getItems().add(definer.getIndex(), definer));
    }

}
