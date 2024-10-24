package com.github.lunarconcerto.mrt.controller;

import com.dustinredmond.fxtrayicon.FXTrayIcon;
import com.github.lunarconcerto.mrt.MRTApplication;
import com.github.lunarconcerto.mrt.MRTStarter;
import com.github.lunarconcerto.mrt.util.Exit;
import com.github.lunarconcerto.mrt.util.I18n;
import javafx.stage.Stage;

import java.util.Objects;

public class TrayIconManager {

    private static final TrayIconManager trayIconManager = new TrayIconManager();

    private FXTrayIcon trayIcon;

    private TrayIconManager() {
    }

    public static TrayIconManager getTrayIconManager() {
        return trayIconManager;
    }

    public FXTrayIcon getTrayIcon() {
        return trayIcon;
    }

    public void initTrayIcon(Stage stage) {
        if (!FXTrayIcon.isSupported()) return;
        if (!MRTApplication.configuration.isEnableTrayIcon()) return;

        trayIcon = new FXTrayIcon.Builder(stage, Objects.requireNonNull(MRTStarter.class.getResource("img/icon.cafe.png")))
                .addTitleItem(true)
                .applicationTitle("MRT")
                .toolTip("MRT")
                .separator()
                .menuItem(I18n.get("tray_icon.item.show_main"),
                        event -> MRTApplication.mainStage.show())
                .menuItem(I18n.get("tray_icon.item.exit_item"),
                        e -> Exit.exit())
                .build();
        trayIcon.show();
    }

}
