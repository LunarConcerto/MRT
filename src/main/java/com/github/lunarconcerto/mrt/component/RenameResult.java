package com.github.lunarconcerto.mrt.component;

import com.github.lunarconcerto.mrt.util.FileNode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RenameResult {
    protected FileNode targetFileNode ;

    protected String targetSourceName , targetNewName ;

    public RenameResult setTargetFileNode(FileNode targetFileNode) {
        this.targetFileNode = targetFileNode;
        return this;
    }

    public RenameResult setTargetSourceName(String targetSourceName) {
        this.targetSourceName = targetSourceName;
        return this;
    }

    public RenameResult setTargetNewName(String targetNewName) {
        this.targetNewName = targetNewName;
        return this;
    }

}
