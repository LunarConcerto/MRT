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
        propertySheet.getItems().add(new BeanProperty(ConfigurationManager.getManager().getConfiguration() ,
                new PropertyDescriptor("config" , Configuration.class)));
    }

    public static void showWindow(){
        try {
            MRTPropertyPaneController controller = new MRTPropertyPaneController();
            FXMLLoader fxmlLoader = new FXMLLoader(MRTStarter.class.getResource("mrt.property.fxml"));
            fxmlLoader.setRoot(controller);
            fxmlLoader.setController(controller);
            Parent parent = fxmlLoader.load();

            controller.init();
            newStage(parent).show();
        } catch (Exception e) {
            throw new MRTRuntimeException(e);
        }
    }

    static Stage newStage(Parent parent){
        Stage stage = new Stage();
        stage.setTitle("设置");
        stage.setScene(new Scene(parent, 400, 800));
        return stage ;
    }


}
