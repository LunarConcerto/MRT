package com.github.lunarconcerto.magicalrenametool;

import com.github.lunarconcerto.magicalrenametool.core.MRTApp;
import javafx.application.Application;

public class MRTStarter {
    public static void main(String[] args) {
        MRTApp.initBeforeUILoad();
        Application.launch(MRTApp.class);
    }

}
