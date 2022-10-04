package com.github.lunarconcerto.mrt.rule;

import com.github.lunarconcerto.mrt.component.RenameTargetContainer;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.Serializable;
import java.util.Arrays;

public abstract class RuleDefiner extends AnchorPane implements Serializable {

    public static int labelCharSize = 12 ;

    /**
     * 表示该面板,
     * 在MRT主面板的 {@link com.github.lunarconcerto.mrt.gui.MRTController#ruleFillingSetter} , {@link com.github.lunarconcerto.mrt.gui.MRTController#ruleReplaceSetter}
     * 中所处的索引值。
     * <p>
     * 该索引值将决定此处定义的规则在何时被触发。
     */
    protected int index ;

    /**
     * 上一个组件的结尾坐标。
     */
    protected int lastComponentLocationX = 0 ;

    /**
     * 组件之间相差的距离
     */
    protected int nextComponentDistance = 15;

    /**
     * 创建该 {@link RuleDefiner} 的对象,
     * 即对应的 {@link Rule} 实现类,
     * 保留该对象的主要原因在于序列化时的定位。
     */
    protected Rule parentRule;

    public RuleDefiner(Rule parentRule) {
        this.parentRule = parentRule;
    }

    public abstract NameEditor createNameEditor();

    public abstract String serialize();

    public abstract String toSampleText();


    public void addLabel(String text){
        Label label = new Label(text);

        int width = text.length() * labelCharSize ;

        label.setLayoutY(13);
        label.setLayoutX(lastComponentLocationX + nextComponentDistance) ;
        label.setMaxWidth(width);
        label.setPrefWidth(width);

        lastComponentLocationX += nextComponentDistance + width;
        addComponent(label);
    }

    public <T> ChoiceBox<T> addChoiceBox(double width , T[] contents){
        ChoiceBox<T> box = new ChoiceBox<>();

        box.setLayoutY(7);
        box.setLayoutX(lastComponentLocationX + nextComponentDistance);
        box.setPrefWidth(width);
        box.setMaxWidth(width);
        Arrays.stream(contents).forEach(content -> box.getItems().add(content));

        lastComponentLocationX += nextComponentDistance + width ;
        addComponent(box);

        return box;
    }

    public TextField addTextField(String preText, double width){
        TextField textField = new TextField(preText);

        textField.setLayoutY(9);
        textField.setLayoutX(lastComponentLocationX + nextComponentDistance);
        textField.setPrefWidth(width);
        textField.setMaxWidth(width);

        lastComponentLocationX += nextComponentDistance + width ;
        addComponent(textField);

        return textField;
    }

    public TextField addTextField(double width){
        return addTextField("" , width);
    }

    public TextField addTextField(){
        return addTextField(100);
    }

    public void addComponent(Node node){
        this.getChildren().add(node);
    }

    public int getIndex() {
        return index;
    }

    public RuleDefiner setIndex(int index) {
        this.index = index;
        return this;
    }

    public Rule getParentRule() {
        return parentRule;
    }

    public static class EmptyRuleDefiner extends RuleDefiner {

        static int index = 0 ;

        public EmptyRuleDefiner() {
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

        static class EmptyNameEditor implements NameEditor{

            @Override
            public void doEdit(RenameTargetContainer builder) {}

        }

    }

}
