package com.github.lunarconcerto.mrt.task;

import javafx.scene.control.ProgressBar;

import java.util.List;

public interface Task<T extends TaskTargetWrapper<?>> {

    void start();

    List<T> getTaskTargetList();

    void setProgressBar(ProgressBar progressBar);

}
