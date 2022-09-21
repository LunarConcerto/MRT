package com.github.lunarconcerto.mrt.component;

import com.github.lunarconcerto.mrt.util.FileNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Optional;

@Getter
public class HandlingObjectContainer {

    protected final FileNode targetPath;

    protected int index ;

    protected String targetSourceName;

    protected StringBuilder newNameBuilder ;

    protected HashMap<String, Object> cache ;

    public HandlingObjectContainer(FileNode targetPath) {
        this.targetPath = targetPath;

        init();
    }

    public HandlingObjectContainer(FileNode targetPath, int index) {
        this.targetPath = targetPath;
        this.index = index;

        init();
    }

    void init(){
        targetSourceName = targetPath.getName();
        cache = new HashMap<>();
        newNameBuilder = new StringBuilder();
    }

    public HandlingObjectContainer putCache(String key, Object value){
        this.cache.put(key, value);
        return this ;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getCache(String key){
        return Optional.of((T) this.cache.get(key));
    }

    public HandlingObjectContainer append(String str){
        newNameBuilder.append(str);
        return this ;
    }

    public HandlingObjectContainer setIndex(int index) {
        this.index = index;
        return this;
    }

}
