package com.github.lunarconcerto.mrt.task;

import java.util.Map;

public abstract class AbstractTaskTargetWrapper<T> implements TaskTargetWrapper<T> {

    protected final T taskTarget;

    protected int index = -1;

    protected boolean skip;

    protected Map<String, Object> wrapperTemp;

    protected Map<String, Object> taskTemp;

    public AbstractTaskTargetWrapper(T taskTarget) {
        this.taskTarget = taskTarget;
    }

    @Override
    public T getTaskTarget() {
        return taskTarget;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public AbstractTaskTargetWrapper<T> setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public Map<String, Object> getWrapperTemp() {
        return wrapperTemp;
    }

    @Override
    public Map<String, Object> getTaskTemp() {
        return taskTemp;
    }

    @Override
    public void setTaskTemp(Map<String, Object> taskTemp) {
        this.taskTemp = taskTemp;
    }

    @Override
    public boolean isSkip() {
        return skip;
    }

    public AbstractTaskTargetWrapper<T> skip() {
        this.skip = true;
        return this;
    }


}
