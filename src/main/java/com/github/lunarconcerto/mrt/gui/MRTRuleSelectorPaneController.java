package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.RuleType;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class MRTRuleSelectorPaneController extends AnchorPane {

    static boolean isExist = false ;

    @FXML
    protected ListView<Rule> ruleList ;

    protected WebView ruleDescription;

    RuleType type ;

    Stage stage ;

    public MRTRuleSelectorPaneController() {}

    @FXML
    protected void onConfirm(ActionEvent e){

    }

    @FXML
    protected void onCancel(ActionEvent e){

    }

    @FXML
    protected void onSelectRule(MouseEvent e){
        if (e.getButton() == MouseButton.PRIMARY){
            ObservableList<Rule> rules = ruleList.getSelectionModel().getSelectedItems();
            if (rules!=null && !rules.isEmpty()){
                Rule rule = rules.get(0);
            }
        }
    }

    void init(){
        ruleList.getItems().add(new TestRule());
        stage.setOnCloseRequest(event -> {
            isExist = false;
        });
    }

    public static void showWindow(RuleType type){
        if (isExist){return;}
        try {
            MRTRuleSelectorPaneController controller = new MRTRuleSelectorPaneController();

            Parent parent = ControllerUtil.loadWindow(controller, "mrt.rule.selector.fxml");
            Stage stage = ControllerUtil.newStage(parent, "选择规则", 600, 400);

            controller.setStage(stage).setType(type).init();

            stage.getIcons().add(new Image(FileUtil.getResourceAsStream("icon.cafe.png")));
            stage.setResizable(false);
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

    static class TestRule implements Rule{

        @Override
        public void init() {

        }

        @Override
        public String getName() {
            return "TestRule";
        }

        @Override
        public RuleType getType() {
            return RuleType.FILLING;
        }

        @Override
        public RuleDefiner createDefiner() {
            return null;
        }

        @Override
        public RuleDefiner createDefiner(String serializedString) {
            return null;
        }
    }

}
