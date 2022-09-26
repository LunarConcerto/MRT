package com.github.lunarconcerto.mrt.component;

import com.github.lunarconcerto.mrt.util.FileNode;

import java.util.HashMap;
import java.util.Optional;

public class FillingObjectContainer {

    protected final FileNode targetFileNode;

    protected int index ;

    protected String targetSourceName;

    protected StringBuilder newNameBuilder ;

    protected HashMap<String, Object> cache ;

    public FillingObjectContainer(FileNode targetFileNode) {
        this.targetFileNode = targetFileNode;

        init();
    }

    public FillingObjectContainer(FileNode targetFileNode, int index) {
        this.targetFileNode = targetFileNode;
        this.index = index;

        init();
    }

    void init(){
        targetSourceName = targetFileNode.getName();
        cache = new HashMap<>();
        newNameBuilder = new StringBuilder();
    }

    public FillingObjectContainer putCache(String key, Object value){
        this.cache.put(key, value);
        return this ;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getCache(String key){
        return Optional.of((T) this.cache.get(key));
    }

    public FillingObjectContainer append(int i){
        newNameBuilder.append(i);
        return this ;
    }

    public FillingObjectContainer append(short i){
        newNameBuilder.append(i);
        return this ;
    }

    public FillingObjectContainer append(long i){
        newNameBuilder.append(i);
        return this ;
    }

    public FillingObjectContainer append(float i){
        newNameBuilder.append(i);
        return this ;
    }

    public FillingObjectContainer append(double i){
        newNameBuilder.append(i);
        return this ;
    }

    public FillingObjectContainer append(char c){
        newNameBuilder.append(c);
        return this ;
    }

    public FillingObjectContainer append(String str){
        newNameBuilder.append(str);
        return this ;
    }

    public FillingObjectContainer setIndex(int index) {
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
