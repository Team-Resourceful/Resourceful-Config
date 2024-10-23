package com.teamresourceful.resourcefulconfig.api.types.options;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigObject;

import java.util.function.Predicate;

public enum EntryType {
    BYTE(type -> type == byte.class || type == Byte.class),
    SHORT(type -> type == short.class || type == Short.class),
    INTEGER(type -> type == int.class || type == Integer.class),
    LONG(type -> type == long.class || type == Long.class),
    FLOAT(type -> type == float.class || type == Float.class),
    DOUBLE(type -> type == double.class || type == Double.class),
    BOOLEAN(type -> type == boolean.class || type == Boolean.class),
    STRING(type -> type == String.class),
    ENUM(Class::isEnum),
    OBJECT(type -> type.isAnnotationPresent(ConfigObject.class)),
    ;

    private final Predicate<Class<?>> predicate;

    EntryType(Predicate<Class<?>> predicate) {
        this.predicate = predicate;
    }

    public boolean test(Class<?> type) {
        return predicate.test(type);
    }

    public boolean isAllowedInArrays() {
        return this != OBJECT;
    }

    public boolean mustBeFinal() {
        return this == OBJECT;
    }
}
