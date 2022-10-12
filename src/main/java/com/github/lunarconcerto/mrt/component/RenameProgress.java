package com.github.lunarconcerto.mrt.component;

import com.github.lunarconcerto.mrt.gui.Dialogs;
import com.github.lunarconcerto.mrt.gui.MRTApp;
import com.github.lunarconcerto.mrt.gui.MRTController;
import com.github.lunarconcerto.mrt.gui.MRTResultConfirmPaneController;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.util.FileNode;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import lombok.extern.log4j.Log4j;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class RenameProgress {

    protected List<NameEditor> fillingEditorList, replaceEditorList ;

    protected List<FileNode> renameTarget ;

    protected ProgressBar bar ;

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull RenameProgress newRenameProgress(@NotNull List<FileNode> renameTarget){
        return new RenameProgress(renameTarget);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull RenameProgress newRenameProgress(@NotNull List<FileNode> renameTarget, ProgressBar bar){
        return new RenameProgress(renameTarget, bar);
    }

    public RenameProgress(List<FileNode> renameTarget) {
        this.renameTarget = renameTarget;
    }

    public RenameProgress(List<FileNode> renameTarget, ProgressBar bar) {
        this.renameTarget = renameTarget;
        this.bar = bar;
    }

    public void start() {
        Worker.startWork(this::startRenameProgress);
    }

    protected void startRenameProgress() {
        MRTController controller = MRTApp.mainController;
        controller.changeStatusLabel("正在统计目标文件/文件夹...");
        List<RenameTargetContainer> targetList = createTargetContainerList();
        controller.changeStatusLabel("正在构建新文件名...");
        List<RenameResult> results = buildTargetNewName(targetList);

        showResultConfirmPane(results);
    }

    protected void showResultConfirmPane(List<RenameResult> results){
        Platform.runLater(() -> {
            MRTResultConfirmPaneController.getDialog(results)
                    .showAndWait()
                    .ifPresentOrElse( this::doRename , () -> {
                        Dialogs.showWarning("运行结束", "结果已取消");
                    });
        });
    }

    protected void doRename(@NotNull List<RenameResult> results){
        if (results.isEmpty()){
            Dialogs.showWarning("运行结束", "结果已取消");
            return;
        }

        MRTApp.mainController.changeStatusLabel("文件操作中...");
        for (int i = 0, resultsSize = results.size(); i < resultsSize; i++) {
            doRename(results.get(i));
            setBarProgress((((i + 1) / (float) resultsSize) / 0.5) + 0.5);
        }

        completeProgress();
    }

    protected void doRename(@NotNull RenameResult result){
        boolean isSusses =
                result.getTargetFileNode().renameTo(new File(result.getNewFullName()));

        if (!isSusses){
            MRTApp.printToUiLogger(
                    "对" + result.getSourceFullName() + "的重命名失败.\n"
            );
        }
    }

    protected void completeProgress(){
        MRTApp.mainController.reset();

        Notifications.create()
                .title("完成")
                .text("分配的重命名任务已全部完成。")
                .showInformation()
        ;
    }

    protected List<RenameResult> buildTargetNewName(@NotNull List<RenameTargetContainer> targetList){
        List<RenameResult> list = new ArrayList<>();
        for (int i = 0, targetListSize = targetList.size(); i < targetListSize; i++) {
            RenameTargetContainer renameTargetContainer = targetList.get(i);
            RenameResult buildTargetNewName = buildTargetNewName(renameTargetContainer);
            list.add(buildTargetNewName);

            setBarProgress(((i + 1) / (float) targetListSize) / 0.5);
        }

        return list;
    }

    protected RenameResult buildTargetNewName(@NotNull RenameTargetContainer container){
        /* 预处理的替换型规则 */
        replaceEditorList.stream().filter(editor -> editor.getEditorRuntime() == NameEditor.EditorRuntime.PRE).forEachOrdered(editor -> editor.doEdit(container));
        /* 运行填充型规则 */
        fillingEditorList.forEach(editor -> editor.doEdit(container));
        /* 对构建的新文件名添加上拓展名 */
        container.append(container.getTargetExtension());
        /* 后处理的替换型规则 */
        replaceEditorList.stream().filter(editor -> editor.getEditorRuntime() == NameEditor.EditorRuntime.POST).forEachOrdered(editor -> editor.doEdit(container));

        log.debug("对原文件 "+ container.targetFileNode.getFullName() + "构建的新文件名为:" + container.newNameBuilder.toString());
        return new RenameResult()
                .setTargetNewName(container.newNameBuilder.toString())
                .setTargetFileNode(container.getTargetFileNode());
    }

    protected List<RenameTargetContainer> createTargetContainerList() {
        List<RenameTargetContainer> result = new ArrayList<>();
        for (int i = 0, renameTargetSize = renameTarget.size(); i < renameTargetSize; i++) {
            FileNode node = renameTarget.get(i);
            RenameTargetContainer container = new RenameTargetContainer(node, i);
            result.add(container);
        }
        return result ;
    }

    protected void setBarProgress(final double progress){
        Platform.runLater(() -> {
            bar.setProgress(progress);
        });
    }

    public RenameProgress setBar(ProgressBar bar) {
        this.bar = bar;
        return this;
    }

    public RenameProgress setFillingEditorList(List<NameEditor> fillingEditorList) {
        this.fillingEditorList = fillingEditorList;
        return this;
    }

    public RenameProgress setReplaceEditorList(List<NameEditor> replaceEditorList) {
        this.replaceEditorList = replaceEditorList;
        return this;
    }

}
