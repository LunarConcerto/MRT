package com.github.lunarconcerto.mrt.task;

import com.github.lunarconcerto.mrt.MRTApplication;
import com.github.lunarconcerto.mrt.controller.IndexController;
import com.github.lunarconcerto.mrt.controller.WindowManager;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.util.Logger;
import com.github.lunarconcerto.mrt.util.NameChecker;
import com.github.lunarconcerto.mrt.util.Worker;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileRenameTask extends TextTargetTask {

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 局部变量
     * * * * * * * * * * * * * * * * * * * * * * * */

    protected List<NameEditor> nameEditorList;

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 静态封装
     * * * * * * * * * * * * * * * * * * * * * * * */

    public FileRenameTask(List<TaskTargetWrapper<String>> taskTargetList) {
        super(taskTargetList);
    }

    public static void newTask(List<File> targetList, ProgressBar bar, List<NameEditor> editorList) {
        FileRenameTask task = new FileRenameTask(createTargetWrapperList(targetList));
        task.setProgressBar(bar);
        task.setNameEditorList(editorList);
        task.start();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    static @NotNull List<TaskTargetWrapper<String>> createTargetWrapperList(@NotNull List<File> targetList) {
        List<TaskTargetWrapper<String>> result = new ArrayList<>();
        for (int i = 0, renameTargetSize = targetList.size(); i < renameTargetSize; i++) {
            result.add(new FileRenameTargetWrapper(targetList.get(i), i));
        }
        return result;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 公有方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    public void start() {
        Worker.startWork(this::startRenameProgress);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 保护方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    protected void startRenameProgress() {
        Logger.info("开始新任务.");
        IndexController controller = MRTApplication.mainController;

        controller.changeStatusLabel("正在构建新文件名...");
        List<RenameResult> results = buildTargetNewName(getTaskTargetList());

        controller.changeStatusLabel("等待确认结果");
        Platform.runLater(() -> {
            Optional<List<RenameResult>> confirmResults = showResultConfirmPane(results);
            if (confirmResults.isPresent() && !confirmResults.get().isEmpty()) {
                controller.changeStatusLabel("文件操作中...");
                doRename(confirmResults.get());
                completeProgress();
            } else {
                WindowManager.showWarning("运行结束", "结果已取消");
                Logger.info("运行中断了.");
            }

            controller.reset();
            controller.unlock();
        });


    }

    protected List<RenameResult> buildTargetNewName(@NotNull List<TaskTargetWrapper<String>> targetList) {
        List<RenameResult> list = new ArrayList<>();
        for (int i = 0, targetListSize = targetList.size(); i < targetListSize; i++) {
            FileRenameTargetWrapper fileRenameTargetWrapper = (FileRenameTargetWrapper) targetList.get(i);
            RenameResult buildTargetNewName = buildTargetNewName(fileRenameTargetWrapper);
            if (buildTargetNewName != null) {
                list.add(buildTargetNewName);
            }

            setBarProgress(((i + 1) / (float) targetListSize) / 0.5);
        }

        return list;
    }

    protected RenameResult buildTargetNewName(@NotNull FileRenameTargetWrapper container) {
        nameEditorList.forEach(editor -> editor.doEdit(container));

        if (container.isSkip()) {
            return null;
        }

        /* 对构建的新文件名添加上拓展名 */
        container.appendExtension();

        String newName = container.getTextBuilder().toString();
        if (NameChecker.isInvalidFileNameString(newName)) {
            newName = NameChecker.replaceInvalidName(newName);
        }

        Logger.debug("对原文件 " + container.targetFile.toString() + "构建的新文件名为:" + newName);

        return new RenameResult()
                .setTargetNewName(newName)
                .setTargetFile(container.getTargetFile());
    }

    protected Optional<List<RenameResult>> showResultConfirmPane(List<RenameResult> results) {
        return WindowManager.showResultConfirmDialog(results).showAndWait();
    }

    protected void doRename(@NotNull List<RenameResult> results) {
        for (int i = 0, resultsSize = results.size(); i < resultsSize; i++) {
            doRename(results.get(i));
            setBarProgress((((i + 1) / (float) resultsSize) / 0.5) + 0.5);
        }
    }

    protected void doRename(@NotNull RenameResult result) {
        boolean isSusses =
                result.getTargetFile().renameTo(new File(result.getNewFullName()));

        if (!isSusses) {
            Logger.info(
                    "对" + result.getSourceFullName() + "的重命名失败.\n"
            );
        }
    }

    protected void completeProgress() {
        Logger.info("分配的重命名任务已全部完成。");
        Notifications.create()
                .title("完成")
                .text("分配的重命名任务已全部完成。")
                .showInformation()
        ;
    }

    protected void setBarProgress(final double progress) {
        Platform.runLater(() -> getProgressBar().setProgress(progress));
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * Setter / Getter
     * * * * * * * * * * * * * * * * * * * * * * * */

    public FileRenameTask setNameEditorList(List<NameEditor> nameEditors) {
        this.nameEditorList = nameEditors;
        return this;
    }

}
