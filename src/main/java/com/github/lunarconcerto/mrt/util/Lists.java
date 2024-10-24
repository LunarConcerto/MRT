package com.github.lunarconcerto.mrt.util;

import java.util.List;

public class Lists {

    private Lists() {
    }

    public static <T> void moveToFirst(List<T> list, T listItem) {
        if (list == null || listItem == null) return;

        list.remove(listItem);
        list.add(0, listItem);
    }

}
