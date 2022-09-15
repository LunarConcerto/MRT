package com.github.lunarconcerto.mrt.core;


import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.BeanPropertyUtils;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.Editors;
import org.controlsfx.property.editor.PropertyEditor;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MRTPropertyPaneController extends AnchorPane {

    @FXML
    public PropertySheet propertySheet ;

    public MRTPropertyPaneController() {

    }

    void init() throws IntrospectionException {
        MRTProperty<String> property = new MRTProperty<>("114514", "测试1", "net");
        MRTProperty<Integer> property1 = new MRTProperty<>(114514, "测试2", "net");
        MRTProperty<Boolean> property2 = new MRTProperty<>(true, "测试3");

        propertySheet.getItems().addAll(property, property1, property2);

        SimpleObjectProperty<Callback<PropertySheet.Item, PropertyEditor<?>>>
                propertyEditorFactory = new SimpleObjectProperty<>(
                        this, "propertyEditor", new DefaultPropertyEditorFactory());

        propertySheet.setPropertyEditorFactory(param -> {
            PropertyEditor<?> editor = propertyEditorFactory.get().call(param);

            //Add listeners to editor
            editor.getEditor()
                    .focusedProperty()
                    .addListener((observable, oldValue, newValue) ->
                            System.out.println(newValue));

            return editor;
        });
    }

    public static void showWindow(){
        try {
            MRTPropertyPaneController controller = new MRTPropertyPaneController();
            Parent parent = ControllerUtil.loadWindow(controller, "mrt.property.fxml");

            controller.init();
            ControllerUtil.newStage(parent, "设置", 400, 600).show();
        } catch (IntrospectionException e) {
            throw new MRTRuntimeException(e);
        }
    }

    static class MRTProperty<T> implements PropertySheet.Item {

        static int index = 0 ;

        protected T value ;

        protected String category,
                name ,
                description = "A mrt property.";

        public MRTProperty(T value) {
            this.value = value;
            this.name = "设置" + index++ ;
            this.category = "默认";
        }

        public MRTProperty(T value, String name) {
            this.value = value;
            this.name = name;
            this.category = "默认";
        }

        public MRTProperty(T value, String name, String category) {
            this.value = value;
            this.name = name;
            this.category = category;
        }

        @Override
        public Class<?> getType() {
            return value.getClass() ;
        }

        @Override
        public String getCategory() {
            return category ;
        }

        @Override
        public String getName() {
            return name ;
        }

        @Override
        public String getDescription() {
            return description ;
        }

        @Override
        public Object getValue() {
            return value ;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void setValue(Object value) {
            if (value.getClass() == this.value.getClass()){
                this.value = (T) value;
            }else {
                throw new MRTRuntimeException("错误的数据类型.");
            }
        }

        @Override
        public Optional<ObservableValue<?>> getObservableValue() {
            return Optional.empty();
        }

        public MRTProperty<T> setDescription(String description) {
            this.description = description;
            return this;
        }

    }

}
