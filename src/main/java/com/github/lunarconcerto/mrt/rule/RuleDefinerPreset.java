package com.github.lunarconcerto.mrt.rule;

import javafx.scene.control.ListView;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class RuleDefinerPreset {

    protected List<RuleDefiner> fillingRuleList;

    protected List<RuleDefiner> replaceRuleList;

    public RuleDefinerPreset() {
        fillingRuleList = new ArrayList<>();
        replaceRuleList = new ArrayList<>();
    }

    public void addToListView(ListView<RuleDefiner> list, @NotNull RuleType type){
        switch (type){
            case FILLING -> addToFillingListView(list);
            case REPLACE -> addToReplaceListView(list);
        }
    }

    public void addToFillingListView(ListView<RuleDefiner> list){
        addToListView(list, fillingRuleList);
    }

    public void addToReplaceListView(ListView<RuleDefiner> list){
        addToListView(list, replaceRuleList);
    }

    void addToListView(ListView<RuleDefiner> list, @NotNull List<RuleDefiner> definerList){
        definerList.forEach(definer -> list.getItems().add(definer.getIndex(), definer));
    }

}
