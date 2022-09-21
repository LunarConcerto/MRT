package com.github.lunarconcerto.mrt.component;

import com.github.lunarconcerto.mrt.util.FileNode;
import lombok.Getter;

@Getter
public class HandlingObjectContainer {

    protected final FileNode targetPath;

    protected String targetSourceName;

    protected StringBuilder newNameBuilder ;

    public HandlingObjectContainer(FileNode targetPath) {
        this.targetPath = targetPath;

        init();
    }

    void init(){
        targetSourceName = targetPath.getName();
        newNameBuilder = new StringBuilder();
    }

}
