package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleType;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Getter;

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

    void init(){
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

}
