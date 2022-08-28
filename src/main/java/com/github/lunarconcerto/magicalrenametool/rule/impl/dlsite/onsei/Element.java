package com.github.lunarconcerto.magicalrenametool.rule.impl.dlsite.onsei;

import java.util.Arrays;

public enum Element {

    NON("未选择", "non"),

    RJ("RJ号", "rj"),

    AUTHOR("サークル名", "author") ,

    DATE("販売日", "date"),

    CV("声優", "cv") ,

    NAME("作品名", "name");

    private final String name ;

    private final String id ;

    Element(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public static Element of(String s){
        return Arrays.stream(Element.values())
                .filter(element -> element.getId().equals(s))
                .findFirst()
                .orElse(NON);

    }

    @Override
    public String toString() {
        return getName();
    }


}
