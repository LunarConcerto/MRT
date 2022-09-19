package com.github.lunarconcerto.mrt.core;


import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 设置面板控制器
 */
public class MRTPropertyPaneController extends AnchorPane {

    @FXML
    public Button buttonApply ;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
    * 网络设置
    * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public ToggleGroup toggleProxyGroup ;

    @FXML
    public TextField textProxyHost ;

    @FXML
    public Spinner<Short> textProxyPort ;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 杂项设置
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public TextField textDefaultPath ;

    @FXML
    public ToggleGroup toggleStickGroup ;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 其他
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected Stage stage ;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public MRTPropertyPaneController() {}

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 底部按钮的触发动作
     * * * * * * * * * * * * * * * * * * * * * * * * * */

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

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 其他方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    void init() {
        System.out.println(toggleProxyGroup.getToggles());
    }

    public MRTPropertyPaneController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 静态方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

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
}
