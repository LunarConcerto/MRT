package com.github.lunarconcerto.mrt.gui;

import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.rule.RuleType;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class MRTRuleSelectorPaneController extends AnchorPane {

    RuleType type ;

    public MRTRuleSelectorPaneController() {}

    void init(){

    };

    public static void showWindow(RuleType type){
        try {
            MRTRuleSelectorPaneController controller = new MRTRuleSelectorPaneController();

            Parent parent = ControllerUtil.loadWindow(controller, "mrt.rule.selector.fxml");
            Stage stage = ControllerUtil.newStage(parent, "规则选择器", 600, 400);

            controller.setType(type).init();

            stage.getIcons().add(new Image(FileUtil.getResourceAsStream("icon.cafe.png")));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            throw new MRTRuntimeException(e);
        }
    }

    public MRTRuleSelectorPaneController setType(RuleType type) {
        this.type = type;
        return this;
    }
}
