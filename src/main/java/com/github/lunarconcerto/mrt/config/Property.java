package com.github.lunarconcerto.mrt.config;

import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import javafx.beans.value.ObservableValue;
import lombok.Getter;
import org.controlsfx.control.PropertySheet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Getter
public class Property<T> implements PropertySheet.Item {

    protected T value ;
    protected final String key ;

    protected String name = "未定义属性";
    protected String category = "默认";

    protected String description = "未定义说明.";

    @Contract(value = "_, _ -> new", pure = true)
    public static <T> @NotNull Property<T> newProperty(T value, String key){
        return new Property<>(value, key);
    }

    public static <T> @NotNull Property<T> newProperty(T value, String key, String name){
        return new Property<>(value, key, name);
    }

    public Property(T value, String key) {
        this.value = value;
        this.key = key;
    }

    public Property(T value, String key, String name) {
        this.value = value;
        this.key = key;
        this.name = name;
    }

    @Override
    public Class<?> getType() {
        return value.getClass() ;
    }

    @Override
    public String getCategory() {
        return category ;
    }

    @Override
    public String getName() {
        return name ;
    }

    @Override
    public String getDescription() {
        return description ;
    }

    @Override
    public Object getValue() {
        return value ;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        if (value.getClass() == this.value.getClass()){
            this.value = (T) value;
        }else {
            throw new MRTRuntimeException("不匹配的类型赋值.");
        }
    }

    @Override
    public Optional<ObservableValue<?>> getObservableValue() {
        return Optional.empty();
    }

    public Property<T> setName(String name) {
        this.name = name;
        return this;
    }

    public Property<T> setCategory(String category) {
        this.category = category;
        return this;
    }

    public Property<T> setDescription(String description) {
        this.description = description;
        return this;
    }
}
