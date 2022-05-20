package com.github.lunarconcerto.magicalrenametool.rule;

import lombok.Data;

@Data
public abstract class AbstractRule implements Rule {

    protected String name ;

    @Override
    public String getName() {
        return name ;
    }

    @Override
    public String toString(){
        return getName() ;
    }

}
