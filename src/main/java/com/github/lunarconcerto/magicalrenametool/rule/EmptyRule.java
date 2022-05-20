package com.github.lunarconcerto.magicalrenametool.rule;

import com.github.lunarconcerto.magicalrenametool.core.RenameToolApplication;
import com.github.lunarconcerto.magicalrenametool.util.FileNode;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class EmptyRule extends AbstractRule{

    public EmptyRule() {
        this.name = "未选择" ;
    }

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
    public List<RenameResult> run(List<FileNode> fileNodeList , ProgressBar bar) {
        RenameToolApplication.printToUIConsole("未选择规则，无法运行。");
        return null;
    }
}
