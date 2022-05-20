package com.github.lunarconcerto.magicalrenametool.func;

import com.github.lunarconcerto.magicalrenametool.core.RenameToolApplication;
import com.github.lunarconcerto.magicalrenametool.rule.RenameResult;
import com.github.lunarconcerto.magicalrenametool.util.FileNode;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class RenameProgress {

    protected final List<RenameResult> target ;

    public RenameProgress(List<RenameResult> target) {
        this.target = target;
    }

    public void run(ProgressBar bar){
        double increment = 1.0 / target.size() , progress = increment ;

        RenameToolApplication.printToUIConsole("开始进行文件操作");

        for (RenameResult renameResult : target) {
            FileNode fileNode = renameResult.getSource();

            String targetNameSource = renameResult.getTargetName();
            if (targetNameSource==null || targetNameSource.equals("")){
                continue;
            }

            String targetName = fileNode.getParent() + "\\" + checkName(targetNameSource) ;
            File targetFile = new File(targetName);

            boolean result = fileNode.renameTo(targetFile);
            if (!result){
                RenameToolApplication.printToUIConsole("重命名失败，可能是因为没有权限，请尝试以管理员权限打开本程序");
            }

            double finalProgress = progress;
            Platform.runLater(() -> {
                bar.setProgress(finalProgress);
            });

            progress+=increment ;
        }

        RenameToolApplication.printToUIConsole("已完成全部的重命名工作");
    }

    private @NotNull String checkName(@NotNull String string){
        return string.replaceAll("[<>:\"|\\\\*?]" , "")
                .replaceAll("/" , "&")
                ;
    }

}
