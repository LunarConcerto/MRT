package com.github.lunarconcerto.mrt.controller;

import com.github.lunarconcerto.mrt.MRTApplication;
import com.github.lunarconcerto.mrt.util.FileUtil;
import com.github.lunarconcerto.mrt.util.PopOvers;
import com.github.lunarconcerto.mrt.util.Texts;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AboutController extends Controller {

    @FXML
    protected Hyperlink linkQQ;

    @FXML
    protected Label title;

    @FXML
    protected Label version;

    @Override
    protected void init() {
        title.setFont(Texts.fontSmileySens(48));
        version.setText(MRTApplication.VERSION);
    }

    @FXML
    protected void openGithub1() {
        MRTApplication.hostServices.showDocument("https://github.com/LunarConcerto/");
    }

    @FXML
    protected void openGithub2() {
        MRTApplication.hostServices.showDocument("https://github.com/LunarConcerto/MRT");
    }

    @FXML
    protected void openQQ() {
        FileUtil.writeStringToClipboard("1399265255");
        PopOvers.showPopOver(linkQQ, "已复制到剪贴板.");
    }
}
