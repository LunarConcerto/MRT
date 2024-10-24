package com.github.lunarconcerto.mrt.control;

import com.github.lunarconcerto.mrt.MRTApplication;
import com.github.lunarconcerto.mrt.controller.WindowManager;
import com.github.lunarconcerto.mrt.rule.NameEditor;
import com.github.lunarconcerto.mrt.util.PopOvers;
import com.github.lunarconcerto.mrt.util.Texts;
import io.github.palexdev.materialfx.controls.MFXContextMenu;
import io.github.palexdev.materialfx.controls.MFXContextMenuItem;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTooltip;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import javafx.collections.ListChangeListener;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RuleDefiningListView extends MFXListView<RuleDefiningPane> {

    @Override
    protected void initialize() {
        super.initialize();

        setCellFactory(this::createCell);
        setBorder(Border.EMPTY);
        getSelectionModel().setAllowsMultipleSelection(false);
        defaultContextMenu();
        defaultDragAction();

        getItems().addListener((ListChangeListener<RuleDefiningPane>) c -> getItems().forEach(item -> item.setIndex(getItems().indexOf(item))));
    }

    protected void defaultContextMenu() {
        MFXContextMenuItem addButton = MFXContextMenuItem.Builder.build()
                .setText("添加")
                .setIcon(Texts.RULE_ADD)
                .setOnAction(event -> WindowManager.showRuleSelector())
                .get();
        MFXTooltip.of(addButton, "添加一个新的规则定义面板.")
                .install();

        MFXContextMenuItem removeButton = MFXContextMenuItem.Builder.build()
                .setText("删除")
                .setIcon(Texts.RULE_REMOVE)
                .setOnAction(event -> {
                    List<RuleDefiningPane> list = getSelectionModel().getSelectedValues();
                    if (!list.isEmpty()) {
                        getItems().remove(list.get(0));
                    }
                })
                .get();
        MFXTooltip.of(removeButton, "删除选中的规则定义面板.")
                .install();

        MFXContextMenuItem copyButton = MFXContextMenuItem.Builder.build()
                .setText("复制")
                .setIcon(Texts.RULE_COPY)
                .setOnAction(event -> {
                    List<RuleDefiningPane> list = getSelectionModel().getSelectedValues();
                    if (!list.isEmpty()) {
                        RuleDefiningPane copyTarget = list.get(0);
                        RuleDefiningPane copyResult = copyTarget.copy();
                        copyResult.setIndex(copyTarget.getIndex() + 1);
                        getItems().add(copyTarget.getIndex() + 1, copyResult);
                    }
                })
                .get();
        MFXTooltip.of(copyButton, "复制选中的规则定义面板.")
                .install();

        MFXContextMenuItem clearButton = MFXContextMenuItem.Builder.build()
                .setText("清空")
                .setIcon(Texts.RULE_CLEAR)
                .setOnAction(event -> {
                    getItems().clear();
                })
                .get();
        MFXTooltip.of(clearButton, "清空当前列表中所有的规则定义面板.\n **注意** 不会保存!!")
                .install();

        MFXContextMenu.Builder builder = MFXContextMenu.Builder.build(this);
        builder.addItems(
                addButton,
                removeButton,
                copyButton,
                clearButton
        );
        builder.installAndGet();
    }

    protected void defaultDragAction() {
        setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });
        setOnDragEntered(event -> {
            if (event.getDragboard().hasContent(RuleDefiningListCell.RULE_DEFINER_LIST_CELL_DATA_FORMAT)) return;

            setOpacity(0.5);
            PopOvers.showPopOver(this, "将文件/文件夹拖放到此处,可直接按当前规则开始运行.");
        });
        setOnDragExited(event -> setOpacity(1));
        setOnDragDropped(event -> {
            if (event.getDragboard().hasContent(RuleDefiningListCell.RULE_DEFINER_LIST_CELL_DATA_FORMAT)) return;

            setOpacity(1);
            if (!event.getDragboard().hasFiles()) {
                WindowManager.showError("无法运行", "剪切板内无文件。");
                return;
            } else if (getItems().isEmpty()) {
                WindowManager.showError("无法运行", "不能开始运行，因为您未指定要进行处理的文件/文件夹。");
                return;
            }

            File file = event.getDragboard().getFiles().get(0);
            MRTApplication.mainController.startNewTask(Collections.singletonList(file), getNameEditors());
        });
    }

    public void swapItem(int a, int b) {
        getItems().get(a).setIndex(b);
        getItems().get(b).setIndex(a);

        Collections.swap(getItems(), a, b);
    }

    public List<NameEditor> getNameEditors() {
        return this.getItems()
                .stream()
                .map(RuleDefiningPane::createNameEditor)
                .collect(Collectors.toList());
    }

    @Contract("_ -> new")
    private @NotNull MFXListCell<RuleDefiningPane> createCell(RuleDefiningPane ruleDefiningPane) {
        return new RuleDefiningListCell(this, ruleDefiningPane);
    }

}
