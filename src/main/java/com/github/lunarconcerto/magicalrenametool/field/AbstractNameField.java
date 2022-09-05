package com.github.lunarconcerto.magicalrenametool.field;

public abstract class AbstractNameField implements NameField {

    protected String name ;

    protected int index ;

    public AbstractNameField() {}

    public AbstractNameField(String name, int index) {
        this.name = name;
        this.index = index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setName(String name) {
        this.name = name;
    }
}
