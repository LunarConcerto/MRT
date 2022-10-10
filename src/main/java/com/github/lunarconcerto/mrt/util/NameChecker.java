package com.github.lunarconcerto.mrt.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NameChecker {

    public static final String[] INVALID_CHAR =
            { "\\" , "/" , ":" , "*" , "?" , "\"" , "<" , ">" , "|"};

    private NameChecker() {}

    public static boolean isInvalidFileName(@Nullable String name){
        if (name==null || name.isBlank() || name.isEmpty()){
            return true ;
        }

        return Arrays.stream(INVALID_CHAR)
                .anyMatch(name::contains);
    }

    public static boolean isValidFileName(@Nullable String name){
        return !isInvalidFileName(name);
    }

    public static boolean isAllValid(@Nullable List<String> names){
        if (names == null || names.isEmpty() ){
            return false ;
        }

        return names.stream().noneMatch(NameChecker::isInvalidFileName);
    }

    public static boolean isAllValid(String[] names){
        if (names == null || names.length == 0){
            return false ;
        }

        return isAllValid(new ArrayList<>(List.of(names)));
    }

    public static boolean isAllInvalid(@Nullable List<String> names){
        return !isAllValid(names);
    }

    public static boolean isAllInvalid(@Nullable String[] names){
        return !isAllValid(names);
    }

}
