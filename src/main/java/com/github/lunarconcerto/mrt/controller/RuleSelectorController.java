package com.github.lunarconcerto.mrt.controller;

import com.github.lunarconcerto.mrt.MRTApplication;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleManager;
import com.github.lunarconcerto.mrt.util.Texts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@NoArgsConstructor
public class RuleSelectorController extends Controller {

    @FXML
    protected ListView<Rule> ruleList;

    @FXML
    protected TextFlow ruleDescription;

    protected IndexController indexController = MRTApplication.mainController;

    @FXML
    protected void onAppend(ActionEvent e) {
        Rule rule = ruleList.getSelectionModel().getSelectedItem();
        if (rule == null) return;

        indexController.addRule(rule);
        close();
    }

    @FXML
    protected void onInsert(ActionEvent e) {
        Rule rule = ruleList.getSelectionModel().getSelectedItem();
        if (rule == null) return;

        List<RuleDefiningPane> values = indexController.ruleList.getSelectionModel().getSelectedValues();
        if (!values.isEmpty()) {
            int index = values.get(0).getIndex();
            indexController.addRule(rule, index);
        }
        close();
    }

    @FXML
    protected void onCancel(ActionEvent e) {
        close();
    }

    @Override
    protected void init() {
        ruleList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) showDescription(newValue);
        });

        ruleList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Rule> call(ListView<Rule> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Rule item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            textProperty().set(item.getName());
                        }
                    }
                };
            }
        });

        List<Rule> items = ruleList.getItems();
        items.addAll(RuleManager.getInstance().getAllRules());
        if (!items.isEmpty()) ruleList.getSelectionModel().selectFirst();

    }

    void showDescription(@NotNull Rule rule) {
        ruleDescription.getChildren().clear();
        ruleDescription.getChildren().add(Texts.text(rule.getName() + "\n", Color.BLACK, Texts.fontSmileySens(24)));
        ruleDescription.getChildren().addAll(rule.getDescription());
    }
}
