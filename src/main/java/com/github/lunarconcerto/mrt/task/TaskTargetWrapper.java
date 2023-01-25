package com.github.lunarconcerto.mrt.task;

import java.util.Map;

public interface TaskTargetWrapper<T> {

    T getTaskTarget();

    int getIndex();

    Map<String, Object> getWrapperTemp();

    Map<String, Object> getTaskTemp();

    void setTaskTemp(Map<String, Object> taskTemp);

    boolean isSkip();

}
