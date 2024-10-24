package com.github.lunarconcerto.mrt.control;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public interface IndexIcon {

    Label getLabel();

    void setVisible(boolean visible);

    Node getNode();

    class CircleIndexIcon extends StackPane implements IndexIcon {

        protected Label label;

        protected Circle circle;

        public CircleIndexIcon() {
            this("", Color.INDIANRED, Color.WHITE, 10);
        }

        public CircleIndexIcon(String index) {
            this(index, Color.INDIANRED, Color.WHITE, 10);
        }

        CircleIndexIcon(String index, Color circleColor, Color textColor, int Radius) {
            label = new Label(index);
            label.setTextFill(textColor);

            circle = new Circle(Radius, circleColor);

            this.getChildren().addAll(
                    circle,
                    label
            );
        }

        @Override
        public Label getLabel() {
            return this.label;
        }

        @Override
        public Node getNode() {
            return this;
        }

        public Circle getCircle() {
            return circle;
        }
    }

}
