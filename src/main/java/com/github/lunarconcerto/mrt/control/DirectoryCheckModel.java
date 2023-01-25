package com.github.lunarconcerto.mrt.control;

import io.github.palexdev.materialfx.selection.TreeSelectionModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DirectoryCheckModel extends TreeSelectionModel<File> {

    private final ListProperty<DirectoryCheckTreeItem> checkedItems = new SimpleListProperty<>(FXCollections.observableArrayList());

    public DirectoryCheckModel() {
        setAllowsMultipleSelection(true);

        checkedItems.addListener((ListChangeListener<DirectoryCheckTreeItem>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(item -> item.setCheckIndex(checkedItems.indexOf(item)));
                } else if (c.wasRemoved()) {
                    IntStream.range(0, checkedItems.size())
                            .forEachOrdered(i -> checkedItems.get(i).setCheckIndex(i));
                }
            }
        });
    }

    public List<DirectoryCheckTreeItem> getSelectedItemList() {
        return getSelectedItems().stream()
                .map(item -> (DirectoryCheckTreeItem) item)
                .collect(Collectors.toList());
    }

    public ObservableList<DirectoryCheckTreeItem> getCheckedItems() {
        return checkedItems.get();
    }

    public ListProperty<DirectoryCheckTreeItem> checkedItemsProperty() {
        return checkedItems;
    }
}
