package com.github.lunarconcerto.mrt.rule.impl.common;

import com.github.lunarconcerto.mrt.rule.AbstractRule;
import com.github.lunarconcerto.mrt.rule.RenameResult;
import com.github.lunarconcerto.mrt.util.FileNode;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class SuffixRule extends AbstractRule {

    @Override
    public AnchorPane getSettingPanel() throws IOException {
        return null;
    }

    @Override
    public void saveSettings(Properties properties) {

    }

    @Override
    public void loadSettings(Properties properties) {

    }

    @Override
    public List<RenameResult> run(List<FileNode> fileNodeList, ProgressBar bar) {
        return null;
    }

}