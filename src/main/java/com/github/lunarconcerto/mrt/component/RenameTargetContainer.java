package com.github.lunarconcerto.mrt.component;

import com.github.lunarconcerto.mrt.util.FileNode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Optional;

@Getter
public class RenameTargetContainer {

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 局部变量
     * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * 该目标的文件对象
     */
    protected final FileNode targetFileNode;

    /**
     * 该目标的序号
     * 序号是在主界面中选择时的顺序来决定的
     */
    protected int index ;

    /**
     * 该目标的原名
     */
    protected String targetSourceName ,

    /**
     * 该目标的拓展名
     * 如果不是文件，则为空
     */
    targetExtension = "" ;

    /**
     * 该目标是否是文件夹
     */
    protected boolean dir ,

    /**
     * 是否跳过该目标
     * 若为true, 则最终确认结果以后
     * 也不会重命名该目标
     */
    skip = false ;

    /**
     * 新文件名的构建者
     */
    protected StringBuilder newNameBuilder ;

    /**
     * 构建新文件名时用于存储某些项目
     * 该表内容由本 {@link RenameTargetContainer} 对象独享
     */
    protected HashMap<String, Object> cache ,

    /**
     * 构建新文件名时用于存储某些项目,
     * 该表内容将与其他 {@link RenameTargetContainer} 对象共享
     */
    globalCache ;

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 构造方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    public RenameTargetContainer(FileNode targetFileNode) {
        this.targetFileNode = targetFileNode;

        init();
    }

    public RenameTargetContainer(FileNode targetFileNode, int index) {
        this.targetFileNode = targetFileNode;
        this.index = index;

        init();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 公有方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    public RenameTargetContainer putCache(String key, Object value){
        this.cache.put(key, value);
        return this ;
    }

    public RenameTargetContainer putGlobalCache(String key, Object value){
        this.globalCache.put(key, value);
        return this ;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getCache(String key){
        return Optional.of((T) this.cache.get(key));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getGlobalCache(String key){
        return Optional.of((T) this.globalCache.get(key));
    }

    public boolean hasChild(){
        return targetFileNode.isDirectory() && targetFileNode.listFiles().length > 0 ;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 私有方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    private void init(){
        targetSourceName = targetFileNode.getName();
        cache = new HashMap<>();
        newNameBuilder = new StringBuilder();

        dir = targetFileNode.isDirectory();
        if (!dir) {
            int i = targetSourceName.lastIndexOf(".");
            if (i!=-1){
                targetExtension = targetSourceName.substring(i);
                targetSourceName = targetSourceName.substring(0 , i);
            }
        }

    }

    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 保护方法
     * * * * * * * * * * * * * * * * * * * * * * * */

    protected void appendExtension(){
        if (!dir){
            this.append(".").append(getTargetExtension());
        }
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
     * Setter / Getter
     * * * * * * * * * * * * * * * * * * * * * * * */

    public RenameTargetContainer setSkip() {
        this.skip = true ;
        return this;
    }

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

    public RenameTargetContainer setTargetExtension(String targetExtension) {
        this.targetExtension = targetExtension;
        return this;
    }

    protected RenameTargetContainer setGlobalCache(HashMap<String, Object> globalCache) {
        this.globalCache = globalCache;
        return this;
    }
}
