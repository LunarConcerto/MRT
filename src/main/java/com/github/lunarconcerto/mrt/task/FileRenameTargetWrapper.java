package com.github.lunarconcerto.mrt.task;

import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class FileRenameTargetWrapper extends TextWrapper {

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 局部变量
     * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * 该目标的文件对象
     */
    protected final File targetFile;
    /**
     * 该目标是否是文件夹
     */
    protected final boolean dir;
    /**
     * 该目标的原名
     */
    protected String targetSourceName;
    /**
     * 该目标的拓展名
     * 如果不是文件，则为空
     */
    protected String targetExtension;
    /**
     * 是否跳过该目标
     * 若为true, 则最终确认结果以后
     * 也不会重命名该目标
     */
    protected boolean skip = false;

    /**
     * 构建新文件名时用于存储某些项目
     * 该表内容由该 {@link FileRenameTargetWrapper} 对象独享
     */
    protected Map<String, Object> cache;

    /**
     * 构建新文件名时用于存储某些项目,
     * 该表内容将与其他 {@link FileRenameTargetWrapper} 对象共享
     */
    protected Map<String, Object> globalCache;

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    public FileRenameTargetWrapper(@NotNull File targetFile) {
        super(targetFile.getName());
        this.targetFile = targetFile;
        dir = targetFile.isDirectory();
        init();
    }

    public FileRenameTargetWrapper(@NotNull File targetFile, int index) {
        super(targetFile.getName());
        this.targetFile = targetFile;
        this.index = index;
        dir = targetFile.isDirectory();
        init();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 公有方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    public boolean hasChild() {
        File[] files = targetFile.listFiles();
        return targetFile.isDirectory() && files != null && files.length > 0;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 私有方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    private void init() {
        cache = new HashMap<>();

        if (!targetFile.isDirectory()) {
            int i = taskTarget.lastIndexOf(".");
            if (i != -1) {
                targetExtension = taskTarget.substring(i + 1);
                targetSourceName = taskTarget.substring(0, i);
            }
        } else {
            targetExtension = "";
            targetSourceName = taskTarget;
        }

    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 保护方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    protected void appendExtension() {
        if (!dir) {
            this.append(".").append(getTargetExtension());
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * Setter / Getter
     * * * * * * * * * * * * * * * * * * * * * * * */

    public boolean isDir() {
        return dir;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public String getTargetSourceName() {
        return targetSourceName;
    }

    public void setTargetExtension(String targetExtension) {
        this.targetExtension = targetExtension;
    }

}
