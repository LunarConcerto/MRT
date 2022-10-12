package com.github.lunarconcerto.mrt.component;

import com.github.lunarconcerto.mrt.util.FileNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
public class RenameResult {
    protected FileNode targetFileNode ;

    protected String targetSourceName , targetNewName ;

    public RenameResult setTargetFileNode(@NotNull FileNode targetFileNode) {
        this.targetFileNode = targetFileNode;
        this.targetSourceName = targetFileNode.getName() ;
        return this;
    }

    public RenameResult setTargetNewName(String targetNewName) {
        this.targetNewName = targetNewName;
        return this;
    }

    public String getSourceFullName(){
        return targetFileNode.getFullName();
    }

    public String getNewFullName(){
        String fullName = getSourceFullName();
        int i = fullName.lastIndexOf("\\");

        return fullName.substring(0 , i+1 ) + targetNewName ;
    }

}
