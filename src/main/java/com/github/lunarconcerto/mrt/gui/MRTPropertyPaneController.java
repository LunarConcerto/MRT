package com.github.lunarconcerto.mrt.gui;


import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.config.Property;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.PropertySheet;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Properties;

/**
 * 设置面板控制器
 */
public class MRTPropertyPaneController extends AnchorPane {

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
    public Spinner<Integer> textProxyPort ;

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

    @FXML
    public PropertySheet uiProperties ;

    protected Stage stage ;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public MRTPropertyPaneController() {}

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 底部按钮的触发动作
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    public void onCancel(){
        stage.close();
    }

    @FXML
    public void onConfirm(){
        Configuration configuration = saveToConfig();
        ConfigurationManager.getManager().save();

        configuration.applyConfig();
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
    }

    @FXML
    public void onEnableHttpProxy(){
        textProxyHost.setEditable(true);
        textProxyPort.setEditable(true);

        textProxyHost.setDisable(false);
        textProxyPort.setDisable(false);
    }

    @FXML
    public void onSelectDefaultPath(){
        File file = new File(textDefaultPath.getText());
        File defaultPath = file.exists() ? ControllerUtil.createNewDirectoryChooser(file) : ControllerUtil.createNewDirectoryChooser();

        if (defaultPath != null){
            textDefaultPath.setText(defaultPath.getPath());
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 其他方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    void init() {
        uiProperties.setMode(PropertySheet.Mode.CATEGORY);

        Configuration configuration = ConfigurationManager.getManager().getConfiguration();
        //—————————————————————————————————
        //代理设置
        //—————————————————————————————————
        textProxyHost.setText(configuration.getProxyHost());
        textProxyPort.increment(Integer.parseInt(configuration.getProxyPort()));
        textProxyPort.getEditor().textProperty().addListener(this::portChangedListener);
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
        //—————————————————————————————————
        //其他设置
        //—————————————————————————————————
        for (Property<?> value : ConfigurationManager.getManager().getPropertyHashMap().values()) {
            uiProperties.getItems().add(value);
        }
    }

    public Configuration saveToConfig(){
        Configuration configuration = ConfigurationManager.getManager().getConfiguration();
        Properties properties = configuration.getCustomProperties();

        configuration.setDefaultPath(textDefaultPath.getText());

        if (toggleHttpProxy.isSelected()) {
            configuration.setEnableProxy(true);
            configuration.setProxyHost(textProxyHost.getText());
            configuration.setProxyPort(String.valueOf(textProxyPort.getValue()));
        }else {
            configuration.setEnableProxy(false);
        }

        uiProperties.getItems().stream()
                .filter(item -> item instanceof Property<?>)
                .forEach(item -> properties.setProperty(((Property<?>) item).getKey(), String.valueOf(item.getValue())));
        
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

            stage.show();
        } catch (Exception e) {
            throw new MRTRuntimeException(e);
        }
    }

    private void portChangedListener(ObservableValue<? extends String> observable, String oldValue, @NotNull String newValue) {
        if (newValue.isEmpty()) {
            textProxyPort.getEditor().setText("0");
            return;
        }

        try {
            Integer.parseInt(newValue);
        } catch (NumberFormatException e) {
            textProxyPort.getEditor().setText(oldValue);
        }
    }
}
