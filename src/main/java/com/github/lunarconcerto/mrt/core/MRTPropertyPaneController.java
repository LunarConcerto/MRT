package com.github.lunarconcerto.mrt.core;

import com.github.lunarconcerto.mrt.MRTStarter;
import com.github.lunarconcerto.mrt.component.ContributeDialog;
import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;

public class MRTPropertyPaneController extends AnchorPane {

    @FXML
    public PropertySheet propertySheet ;

    public MRTPropertyPaneController() {

    }

    void init() throws IntrospectionException {

    }

    public static void showWindow(){
        try {
            MRTPropertyPaneController controller = new MRTPropertyPaneController();
            Parent parent = ControllerUtil.loadWindow(controller, "mrt.property.fxml");

            controller.init();
            ControllerUtil.newStage(parent, "设置", 400, 800).show();
        } catch (IntrospectionException e) {
            throw new MRTRuntimeException(e);
        }
    }

}
