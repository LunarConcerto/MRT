package com.github.lunarconcerto.mrt.task;

import javafx.scene.control.ProgressBar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTask<T extends TaskTargetWrapper<?>> implements Task<T> {

    protected final Map<String, Object> temp;

    protected List<T> taskTargetList;

    protected ProgressBar progressBar;

    public AbstractTask(@NotNull List<T> taskTargetList) {
        this.taskTargetList = taskTargetList;

        temp = new HashMap<>();
        taskTargetList.forEach(t -> t.setTaskTemp(temp));
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Map<String, Object> getTemp() {
        return temp;
    }

    public List<T> getTaskTargetList() {
        return taskTargetList;
    }
}
