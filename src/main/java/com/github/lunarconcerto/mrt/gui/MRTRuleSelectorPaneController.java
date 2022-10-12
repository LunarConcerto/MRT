package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.RuleManager;
import com.github.lunarconcerto.mrt.rule.RuleType;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class MRTRuleSelectorPaneController extends AnchorPane {

    /**
     * 该窗口是否已经存在
     * <p>
     * 一般只希望存在一个该窗口
     */
    static boolean isExist = false ;

    /**
     * 获取到主面板中,
     * 最后选择到的 {@link RuleDefiner} 的 Index
     */
    int selectRuleIndex ;

    @FXML
    protected ListView<Rule> ruleList ;

    @FXML
    protected TextArea ruleDescription ;

    /**
     * 规则类型筛选
     */
    RuleType type ;

    Stage stage ;

    public MRTRuleSelectorPaneController() {}

    @FXML
    protected void onConfirm(ActionEvent e){
        Rule rule = ruleList.getSelectionModel().getSelectedItem();
        if (rule != null){
            MRTApp.mainController.addRule(rule, selectRuleIndex);
        }

        close();
    }

    @FXML
    protected void onCancel(ActionEvent e){
        close();
    }

    @FXML
    protected void onSelectRule(@NotNull MouseEvent e){
        if (e.getButton() == MouseButton.PRIMARY){
            showDescription(ruleList.getSelectionModel().getSelectedItem());
        }
    }

    void showDescription(Rule rule){
        if (rule!=null){
            ruleDescription.setText(rule.getDescription());
        }else {
            ruleDescription.clear();
        }
    }

    void init(){
        ruleList.setCellFactory(new RuleListCellFactory());
        loadRuleList();
        if (!ruleList.getItems().isEmpty()) {
            ruleList.getSelectionModel().select(0);
            showDescription(ruleList.getItems().get(0));
        }


        switch (type){
            case FILLING -> selectRuleIndex = MRTApp.mainController.ruleFillingSetter.getSelectionModel().getSelectedIndex();
            case REPLACE -> selectRuleIndex = MRTApp.mainController.ruleReplaceSetter.getSelectionModel().getSelectedIndex();
            /* 无选择的话在最后追加 */
            default -> selectRuleIndex = -1 ;
        }

        stage.setOnCloseRequest(event -> isExist = false);
    }

    void loadRuleList(){
        switch (type){
            case FILLING -> addRules(RuleManager.getInstance().getFillingRule());
            case REPLACE -> addRules(RuleManager.getInstance().getReplaceRule());
        }
    }

    void addRules(List<Rule> rules){
        this.ruleList.getItems().addAll(rules);
    }

    void close(){
        stage.close();
        isExist = false ;
    }

    public static void showWindow(RuleType type){
        if (isExist) return;
        try {
            MRTRuleSelectorPaneController controller = new MRTRuleSelectorPaneController();

            Parent parent = ControllerUtil.loadWindow(controller, "mrt.rule.selector.fxml");
            Stage stage = ControllerUtil.newStage(parent, "选择规则", 600, 400);

            controller.setStage(stage).setType(type).init();

            isExist = true ;
            stage.show();
        } catch (Exception e) {
            throw new MRTRuntimeException(e);
        }
    }

    public MRTRuleSelectorPaneController setType(RuleType type) {
        this.type = type;
        return this;
    }

    public MRTRuleSelectorPaneController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    static class RuleListCellFactory implements Callback<ListView<Rule>, ListCell<Rule>> {
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
    }

}
