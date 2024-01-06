package com.teamresourceful.resourcefulconfig.api.config;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigObject;

import java.util.function.Predicate;

public enum EntryType {
    BYTE(type -> type == byte.class),
    SHORT(type -> type == short.class),
    INTEGER(type -> type == int.class),
    LONG(type -> type == long.class),
    FLOAT(type -> type == float.class),
    DOUBLE(type -> type == double.class),
    BOOLEAN(type -> type == boolean.class),
    STRING(type -> type == String.class),
    ENUM(false, Class::isEnum),
    OBJECT(false, type -> type.isAnnotationPresent(ConfigObject.class))
    ;

    private final boolean arrayed;
    private final Predicate<Class<?>> predicate;

    EntryType(boolean arrayed, Predicate<Class<?>> predicate) {
        this.arrayed = arrayed;
        this.predicate = predicate;
    }

    EntryType(Predicate<Class<?>> predicate) {
        this(true, predicate);
    }

    public boolean isAllowedInArrays() {
        return arrayed;
    }

    public boolean test(Class<?> type) {
        return predicate.test(type);
    }

    public boolean mustBeFinal() {
        return this == OBJECT;
    }
}
