package com.github.lunarconcerto.mrt.task;

public class TextWrapper extends AbstractTaskTargetWrapper<String> {

    /**
     * 新文字的构建者
     */
    protected StringBuilder textBuilder = new StringBuilder();

    public TextWrapper(String taskTarget) {
        super(taskTarget);

    }


    /* * * * * * * * * * * * * * * * * * * * * * * *
     * 内部StringBuilder的再封装
     * * * * * * * * * * * * * * * * * * * * * * * */

    public TextWrapper append(int i) {
        textBuilder.append(i);
        return this;
    }

    public TextWrapper append(short i) {
        textBuilder.append(i);
        return this;
    }

    public TextWrapper append(long i) {
        textBuilder.append(i);
        return this;
    }

    public TextWrapper append(float i) {
        textBuilder.append(i);
        return this;
    }

    public TextWrapper append(double i) {
        textBuilder.append(i);
        return this;
    }

    public TextWrapper append(char c) {
        textBuilder.append(c);
        return this;
    }

    public TextWrapper append(String str) {
        textBuilder.append(str);
        return this;
    }

    public TextWrapper replace(String target, String replacement) {
        textBuilder = new StringBuilder(textBuilder.toString().replace(target, replacement));
        return this;
    }

    public StringBuilder getTextBuilder() {
        return textBuilder;
    }

    public void setTextBuilder(StringBuilder textBuilder) {
        this.textBuilder = textBuilder;
    }
}
