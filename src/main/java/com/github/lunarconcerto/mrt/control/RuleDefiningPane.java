package com.github.lunarconcerto.mrt.control;

import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.io.DefinedRuleInfo;
import com.github.lunarconcerto.mrt.task.FileRenameTargetWrapper;
import com.github.lunarconcerto.mrt.util.NameChecker;
import com.github.lunarconcerto.mrt.util.PopOvers;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.Arrays;

public abstract class RuleDefiningPane extends GridPane implements Serializable {

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 属性
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * 表示该面板,
     * 在MRT主面板的 ,
     * 中所处的索引值。
     * <p>
     * 该索引值将决定此处定义的规则在何时被触发。
     */
    protected IntegerProperty index = new SimpleIntegerProperty();


    /**
     * 创建该 {@link RuleDefiningPane} 的对象,
     * 即对应的 {@link Rule} 实现类,
     * 保留该对象的主要原因在于序列化时的定位。
     */
    protected Rule parentRule;

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public RuleDefiningPane(Rule parentRule) {
        this.parentRule = parentRule;
        setAlignment(Pos.CENTER);
        defaultIndexLabel();

        if (parentRule != null) addLabel(parentRule.getName());
    }

    protected void defaultIndexLabel() {
        Label label = new Label("<" + getIndex() + ">");
        label.setTextFill(Color.BLACK);
        indexProperty().addListener((observable, oldValue, newValue) -> {
            label.setText("<" + newValue.toString() + ">");
        });

        addComponent(label, 30);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 抽象方法
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public abstract NameEditor createNameEditor();

    public abstract String serialize();

    public abstract String toSampleText();

    protected void init() {
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 转换封装
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public DefinedRuleInfo toDefinedInfo() {
        return DefinedRuleInfo.create(this);
    }

    public RuleDefiningPane copy() {
        return parentRule.createDefiningPane(this.serialize());
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * 添加控件封装
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected void addLabel(String text) {
        Label label = new Label(text);
        addComponent(label);
    }

    protected <T> MFXLegacyComboBox<T> addChoiceBox(T[] contents) {
        MFXLegacyComboBox<T> box = new MFXLegacyComboBox<>();
        box.setPrefWidth(110);
        Arrays.stream(contents).forEach(content -> box.getItems().add(content));
        addComponent(box);
        return box;
    }

    protected MFXTextField addTextField() {
        return addTextField("");
    }

    protected MFXTextField addTextField(String preText) {
        return addTextField(preText, "");
    }

    protected MFXTextField addTextField(String preText, String promptText) {
        MFXTextField textField = new MFXTextField(preText, promptText);
        textField.setBorderGap(3.0);
        textField.setPrefWidth(110);
        textField.setFloatMode(FloatMode.ABOVE);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (NameChecker.isInvalidFileNameString(newValue)) {
                textField.setText(oldValue);
                PopOvers.showPopOver(textField, " 文件名不能包含下列任何字符: ",
                        "     \\ / : * ? \" < > |");
            }
        });
        addComponent(textField);
        return textField;
    }

    protected MFXToggleButton addToggleButton(String text) {
        MFXToggleButton button = new MFXToggleButton(text);
        addComponent(button);
        return button;
    }

    protected void addComponent(Node node) {
        addComponent(node, 120);
    }

    protected void addComponent(Node node, double width) {
        ColumnConstraints constraints = new ColumnConstraints(width);
        constraints.setHgrow(Priority.NEVER);
        getColumnConstraints().add(constraints);
        setColumnIndex(node, getColumnCount() - 1);
        getChildren().add(node);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * *
     * Setter / Getter
     * * * * * * * * * * * * * * * * * * * * * * * * * */

    public int getIndex() {
        return index.get();
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public IntegerProperty indexProperty() {
        return index;
    }

    public Rule getParentRule() {
        return parentRule;
    }

    public static class EmptyRuleDefiningPane extends RuleDefiningPane {

        static int index = 0;

        public EmptyRuleDefiningPane() {
            super(null);
            addLabel("I am useless rule pane No." + index);

            index++;
        }

        @Override
        public NameEditor createNameEditor() {
            return new EmptyNameEditor();
        }

        @Override
        public String serialize() {
            return "" + index;
        }

        @Override
        public String toSampleText() {
            return "";
        }

        static class EmptyNameEditor implements NameEditor {

            @Override
            public void doEdit(FileRenameTargetWrapper builder) {
            }

        }

    }

}
