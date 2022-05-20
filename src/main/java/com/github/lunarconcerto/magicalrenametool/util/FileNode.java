package com.github.lunarconcerto.magicalrenametool.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class FileNode extends File {

    protected boolean root = false ;

    protected int level;

    public FileNode(@NotNull String pathname) {
        super(pathname);
    }

    public FileNode(@NotNull File file) {
        super(file.getAbsolutePath());
    }

    @Nullable
    @Override
    public FileNode[] listFiles() {
        File[] listFiles = super.listFiles();

        if (listFiles==null){
            return new FileNode[0];
        }

        FileNode[] fileNodes = new FileNode[listFiles.length];
        for (int i = 0; i < listFiles.length; i++) {
            fileNodes[i] = new FileNode(listFiles[i]);
        }

        return fileNodes ;
    }

    @Override
    public String toString() {
        if (root){
            return super.toString();
        }else {
            return this.getName() ;
        }
    }

    public String getFullName(){
        return super.toString();
    }

    public void setRoot() {
        this.root = true ;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
