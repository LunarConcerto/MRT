package com.github.lunarconcerto.mrt.component;

import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Preview extends Stage {

    public static Preview PREVIEW;

    public static Preview getPreview(){
        if (PREVIEW == null) {
            PREVIEW = new Preview();
        }

        return PREVIEW;
    }

    private Preview() {

    }

    public void init(){
        this.setTitle("Preview");
        this.setHeight(100);
        this.setWidth(800);
        this.setResizable(false);

        ScrollPane pane = new ScrollPane();

        this.show();
    }

}
