package com.github.lunarconcerto.mrt.rule;

import javafx.scene.control.ListView;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class RuleDefinerPreset {

    protected List<RuleDefiner> ruleDefinerList;

    public RuleDefinerPreset() {
        ruleDefinerList = new ArrayList<>();
    }

    public void addToListView(@NotNull ListView<RuleDefiner> list){
        addToListView(list , ruleDefinerList);
    }

    void addToListView(@NotNull ListView<RuleDefiner> list, @NotNull List<RuleDefiner> definerList){
        list.getItems().clear();
        definerList.forEach(definer -> list.getItems().add(definer.getIndex(), definer));
    }

}
