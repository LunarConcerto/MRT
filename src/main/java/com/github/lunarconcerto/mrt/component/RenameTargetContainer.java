package com.github.lunarconcerto.mrt.component;

import com.github.lunarconcerto.mrt.util.FileNode;

import java.util.HashMap;
import java.util.Optional;

public class RenameTargetContainer {

    protected final FileNode targetFileNode;

    protected int index ;

    protected String targetSourceName;

    protected StringBuilder newNameBuilder ;

    protected HashMap<String, Object> cache ;

    public RenameTargetContainer(FileNode targetFileNode) {
        this.targetFileNode = targetFileNode;

        init();
    }

    public RenameTargetContainer(FileNode targetFileNode, int index) {
        this.targetFileNode = targetFileNode;
        this.index = index;

        init();
    }

    void init(){
        targetSourceName = targetFileNode.getName();
        cache = new HashMap<>();
        newNameBuilder = new StringBuilder();
    }

    public RenameTargetContainer putCache(String key, Object value){
        this.cache.put(key, value);
        return this ;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getCache(String key){
        return Optional.of((T) this.cache.get(key));
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
    * 内部StringBuilder的再封装
    *
    * append
    * * * * * * * * * * * * * * * * * * * * * * * */

    public RenameTargetContainer append(int i){
        newNameBuilder.append(i);
        return this ;
    }

    public RenameTargetContainer append(short i){
        newNameBuilder.append(i);
        return this ;
    }

    public RenameTargetContainer append(long i){
        newNameBuilder.append(i);
        return this ;
    }

    public RenameTargetContainer append(float i){
        newNameBuilder.append(i);
        return this ;
    }

    public RenameTargetContainer append(double i){
        newNameBuilder.append(i);
        return this ;
    }

    public RenameTargetContainer append(char c){
        newNameBuilder.append(c);
        return this ;
    }

    public RenameTargetContainer append(String str){
        newNameBuilder.append(str);
        return this ;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 内部StringBuilder的再封装
     *
     * replace
     * * * * * * * * * * * * * * * * * * * * * * * */

    public RenameTargetContainer replace(String target, String replacement){
        newNameBuilder = new StringBuilder(newNameBuilder.toString().replace(target, replacement));
        return this ;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 其他
     * * * * * * * * * * * * * * * * * * * * * * * */

    public boolean hasChild(){
        return targetFileNode.isDirectory() && targetFileNode.listFiles().length > 0 ;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * Setter / Getter
     * * * * * * * * * * * * * * * * * * * * * * * */

    public RenameTargetContainer setIndex(int index) {
        this.index = index;
        return this;
    }

    public FileNode getTargetFileNode() {
        return targetFileNode;
    }

    public int getIndex() {
        return index;
    }

    public String getTargetSourceName() {
        return targetSourceName;
    }

}
