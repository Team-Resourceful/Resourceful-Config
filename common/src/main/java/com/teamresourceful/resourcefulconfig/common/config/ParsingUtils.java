package com.teamresourceful.resourcefulconfig.common.config;

import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ParsingUtils {

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(Object value) {
        return new ArrayList<>(List.of((T[]) value));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Enum<?> getEnum(Class<?> clazz, String name) {
        try {
            return Enum.valueOf((Class<Enum>) clazz, name.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getField(Field field) {
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public static ConfigEntry assertEntry(Field field) {
        ConfigEntry data = field.getAnnotation(ConfigEntry.class);
        if (data == null) return null;
        if (!Modifier.isPublic(field.getModifiers())) throw new IllegalArgumentException("Entry " + field.getName() + " is not public!");
        if (!Modifier.isStatic(field.getModifiers())) throw new IllegalArgumentException("Entry " + field.getName() + " is not static!");
        if (Modifier.isFinal(field.getModifiers())) throw new IllegalArgumentException("Entry " + field.getName() + " is final!");
        Class<?> type = field.getType();
        if (type.isArray()) {
            if (!data.type().isAllowedInArrays()) {
                throw new IllegalArgumentException("Entry " + field.getName() + " is an array but its type is not allowed in arrays!");
            }
            type = type.componentType();
        }
        if (!data.type().test(type)) throw new IllegalArgumentException("Entry " + field.getName() + " is not of type " + data.type().name() + "!");
        if (data.id().contains(".")) throw new IllegalArgumentException("Entry " + field.getName() + " has an invalid id! Ids must not contain '.'");
        return data;
    }

    public static void assertValidClass(Class<?> config, Class<? extends Annotation> annotation) {
        if (!Modifier.isPublic(config.getModifiers())) throw new IllegalArgumentException("Config class must be public!");
        if (config.getEnclosingClass() != null && !Modifier.isStatic(config.getModifiers())) throw new IllegalArgumentException("Config class must be static!");
        if (!Modifier.isFinal(config.getModifiers())) throw new IllegalArgumentException("Config class must be final!");
        if (!config.isAnnotationPresent(annotation)) throw new IllegalArgumentException("Config class must be annotated with @" + annotation.getSimpleName() + "!");
        if (config.isEnum()) throw new IllegalArgumentException("Config class cannot be an enum!");
        if (config.isInterface()) throw new IllegalArgumentException("Config class cannot be an interface!");
        if (config.isAnnotation()) throw new IllegalArgumentException("Config class cannot be an annotation!");
        if (config.isRecord()) throw new IllegalArgumentException("Config class cannot be a record!");
    }
}
