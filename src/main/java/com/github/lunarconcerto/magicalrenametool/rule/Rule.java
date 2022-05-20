package com.github.lunarconcerto.magicalrenametool.rule;

import com.github.lunarconcerto.magicalrenametool.util.FileNode;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public interface Rule {

    String getName();

    AnchorPane getSettingPanel() throws IOException;

    void saveSettings(Properties properties);

    void loadSettings(Properties properties);

    List<RenameResult> run(List<FileNode> fileNodeList , ProgressBar bar);

}
