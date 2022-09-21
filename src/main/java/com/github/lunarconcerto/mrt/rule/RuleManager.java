package com.github.lunarconcerto.mrt.rule;

import lombok.Getter;

import java.util.List;

@Getter
public class RuleManager {

    protected List<Rule> aliveRules ;

    private static RuleManager instance ;

    public RuleManager getInstance(){
        if (instance==null){
            instance = new RuleManager();
        }

        return instance;
    }

    

}
