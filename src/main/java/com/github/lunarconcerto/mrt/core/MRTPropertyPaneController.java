package com.github.lunarconcerto.mrt.core;


import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.PropertySheet;

public class MRTPropertyPaneController extends AnchorPane {

    @FXML
    public PropertySheet propertySheet ;

    @FXML
    public Button buttonApply ;

    @FXML
    public ToggleGroup toggleStickGroup ;

    @FXML
    public ToggleGroup toggleProxyGroup ;

    protected Stage stage ;

    public static MRTPropertyPaneController controller ;

    public MRTPropertyPaneController() {

    }

    @FXML
    public void onApply(){
        buttonApply.setDisable(true);

    }

    @FXML
    public void onCancel(){
        stage.close();
    }

    @FXML
    public void onConfirm(){
        stage.close();
    }

    void init() {
        System.out.println(toggleProxyGroup.getToggles());
    }

    public static void showWindow(){
        try {
            MRTPropertyPaneController controller = new MRTPropertyPaneController();

            Parent parent = ControllerUtil.loadWindow(controller, "mrt.property.fxml");
            Stage stage = ControllerUtil.newStage(parent, "设置", 400, 600);

            controller.setStage(stage);
            controller.init();

            stage.getIcons().add(new Image(FileUtil.getResourceAsStream("icon.cafe.png")));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            throw new MRTRuntimeException(e);
        }
    }

    public MRTPropertyPaneController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }
}
