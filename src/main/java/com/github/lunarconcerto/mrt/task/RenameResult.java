package com.github.lunarconcerto.mrt.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;

@Getter
@NoArgsConstructor
public class RenameResult {
    protected File targetFile;

    protected String targetSourceName, targetNewName;

    public RenameResult setTargetFile(@NotNull File targetFile) {
        this.targetFile = targetFile;
        this.targetSourceName = targetFile.getName();
        return this;
    }

    public RenameResult setTargetNewName(String targetNewName) {
        this.targetNewName = targetNewName;
        return this;
    }

    public String getSourceFullName() {
        return targetFile.toString();
    }

    public String getNewFullName() {
        String fullName = getSourceFullName();
        int i = fullName.lastIndexOf("\\");

        return fullName.substring(0, i + 1) + targetNewName;
    }

    @Override
    public String toString() {
        return "RenameResult{" +
                "targetFile=" + targetFile +
                ", targetSourceName='" + targetSourceName + '\'' +
                ", targetNewName='" + targetNewName + '\'' +
                '}';
    }
}
