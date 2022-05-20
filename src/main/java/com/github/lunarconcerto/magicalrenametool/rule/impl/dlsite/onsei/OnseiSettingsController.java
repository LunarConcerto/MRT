package com.github.lunarconcerto.magicalrenametool.rule.impl.dlsite.onsei;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OnseiSettingsController extends AnchorPane{


    @FXML
    public TextField samples ;

    @FXML
    public ComboBox<Element> element1;
    @FXML
    public ComboBox<Element> element2;
    @FXML
    public ComboBox<Element> element3;
    @FXML
    public ComboBox<Element> element4;
    @FXML
    public ComboBox<Element> element5;

    @FXML
    public TextField prefix1;
    @FXML
    public TextField prefix2;
    @FXML
    public TextField prefix3;
    @FXML
    public TextField prefix4;
    @FXML
    public TextField prefix5;

    @FXML
    public TextField suffix1;
    @FXML
    public TextField suffix2;
    @FXML
    public TextField suffix3;
    @FXML
    public TextField suffix4;
    @FXML
    public TextField suffix5;

    protected List<Column> columnList ;

    protected FXMLLoader fxmlLoader ;

    public OnseiSettingsController() {

    }

    @FXML
    public void onChanged(@NotNull ActionEvent event){
        createSamples();
    }

    public AnchorPane loadFXML() throws IOException {
        if (fxmlLoader==null){
            fxmlLoader = new FXMLLoader(DlsiteOnseiRule.class.getResource("dlsite_onsei_settings_pane.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load() ;

            initUI();
        }
        return this ;
    }

    public void createSamples(){
        StringBuilder builder = new StringBuilder();
        for (Column column : columnList) {
            Element element = column.getElement().getSelectionModel().getSelectedItem();
            if (element!=Element.NON){
                builder.append(column.getPrefix().getText())
                        .append(element.getName())
                        .append(column.getSuffix().getText());
            }

        }

        samples.setText(builder.toString());
    }

    protected void initUI(){
        this.createColumnList();

        for (Column column : columnList) {
            column.getElement().getItems().addAll(Element.values());

            column.getElement().getSelectionModel().select(Element.NON);
        }

    }

    public void createColumnList(){
        columnList = new ArrayList<>(5);

        columnList.add(0,new Column(prefix1, suffix1, element1));
        columnList.add(1,new Column(prefix2, suffix2, element2));
        columnList.add(2,new Column(prefix3, suffix3, element3));
        columnList.add(3,new Column(prefix4, suffix4, element4));
        columnList.add(4,new Column(prefix5, suffix5, element5));

        for (Column column : columnList) {
            column.getSuffix().setOnAction(this::onChanged);
            column.getElement().setOnAction(this::onChanged);
            column.getPrefix().setOnAction(this::onChanged);
        }
    }
}
