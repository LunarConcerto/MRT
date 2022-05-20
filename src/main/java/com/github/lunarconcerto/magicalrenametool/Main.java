package com.github.lunarconcerto.magicalrenametool;

import com.github.lunarconcerto.magicalrenametool.core.RenameToolApplication;
import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        RenameToolApplication.initBeforeUILoad();
        Application.launch(RenameToolApplication.class);
    }

}
