package com.github.lunarconcerto.mrt;

import com.github.lunarconcerto.mrt.core.MRTApp;
import javafx.application.Application;

public class MRTStarter {
    public static void main(String[] args) {
        MRTApp.initBeforeUILoad();
        Application.launch(MRTApp.class);
    }

}