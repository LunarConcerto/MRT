package com.github.lunarconcerto.mrt.core;


import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;

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
    public RadioButton toggleNoProxy , toggleHttpProxy ;

    @FXML
    public TextField textProxyHost ;

    @FXML
    public Spinner<Short> textProxyPort ;

    @FXML
    public RadioButton stickTrue , stickFalse;

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
     * 主要设置相关的触发方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public void onDisableProxy(){
        textProxyHost.setEditable(false);
        textProxyPort.setEditable(false);

        textProxyHost.setDisable(true);
        textProxyPort.setDisable(true);

        resetApplyButton();
    }

    @FXML
    public void onEnableHttpProxy(){
        textProxyHost.setEditable(true);
        textProxyPort.setEditable(true);

        textProxyHost.setDisable(false);
        textProxyPort.setDisable(false);

        resetApplyButton();
    }

    @FXML
    public void onSelectDefaultPath(){
        File file = new File(textDefaultPath.getText());
        File defaultPath = file.exists() ? ControllerUtil.createNewDirectoryChooser(file) : ControllerUtil.createNewDirectoryChooser();

        if (defaultPath != null){
            textDefaultPath.setText(defaultPath.getPath());
        }

        resetApplyButton();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 其他方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public void resetApplyButton(){
        buttonApply.setDisable(false);
    }

    void init() {
        buttonApply.setDisable(true);
        Configuration configuration = ConfigurationManager.getManager().getConfiguration();
        //—————————————————————————————————
        //代理设置
        //—————————————————————————————————
        if (configuration.isEnableProxy()) {
            toggleProxyGroup.selectToggle(toggleHttpProxy);
            onEnableHttpProxy();
        } else {
            toggleProxyGroup.selectToggle(toggleNoProxy);
            onDisableProxy();
        }
        //—————————————————————————————————
        //默认打开路径
        //—————————————————————————————————
        textDefaultPath.setText(configuration.getDefaultPath());
        //—————————————————————————————————
        //置顶
        //—————————————————————————————————
        toggleStickGroup.selectToggle(configuration.isEnableStick() ? stickTrue : stickFalse);
    }

    public Configuration saveToConfig(){
        Configuration configuration = ConfigurationManager.getManager().getConfiguration();

        configuration.setDefaultPath(textDefaultPath.getText());

        if (toggleHttpProxy.isSelected()) {
            configuration.setEnableProxy(true);
            configuration.setProxyHost(textProxyHost.getText());
            configuration.setProxyPort(String.valueOf(textProxyPort.getValue()));
        }else {
            configuration.setEnableProxy(false);
        }
        
        return configuration ;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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
