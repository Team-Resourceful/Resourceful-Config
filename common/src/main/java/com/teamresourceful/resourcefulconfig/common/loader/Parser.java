package com.teamresourceful.resourcefulconfig.common.loader;

import com.teamresourceful.resourcefulconfig.api.annotations.Category;
import com.teamresourceful.resourcefulconfig.api.annotations.Config;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigButton;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.entries.Observable;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.info.ParsedInfo;
import com.teamresourceful.resourcefulconfig.common.loader.buttons.ParsedButton;
import com.teamresourceful.resourcefulconfig.common.loader.entries.ParsedInstanceEntry;
import com.teamresourceful.resourcefulconfig.common.loader.entries.ParsedObjectEntry;
import com.teamresourceful.resourcefulconfig.common.loader.entries.ParsedObservableEntry;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Parser {

    public static ResourcefulConfig parse(Class<?> clazz) {
        Config data = assertAnnotation(clazz, Config.class);
        return populateEntries(clazz, new ParsedConfig(data, ParsedInfo.of(clazz, data.value())), data.categories());
    }

    private static <T extends ResourcefulConfig> T populateEntries(Class<?> clazz, T config, Class<?>[] categories) {
        assertValidClass(clazz);

        for (Field field : clazz.getDeclaredFields()) {
            ConfigEntry data = assertEntry(field);
            if (data != null) {
                if (data.type() == EntryType.OBJECT) {
                    Object instance = ParsingUtils.getField(field, null);
                    ParsedObjectEntry objectEntry = new ParsedObjectEntry(data.type(), field);
                    populateEntries(instance, objectEntry);
                    config.entries().put(data.id(), objectEntry);
                } else if (field.getType() == Observable.class) {
                    ParsedObservableEntry observableEntry = ParsedObservableEntry.of(data.type(), field, null);
                    config.entries().put(data.id(), observableEntry);
                    if (observableEntry.defaultValue() == null) {
                        throw new IllegalArgumentException("Entry " + field.getName() + " must not have a null default value!");
                    }
                } else {
                    ParsedInstanceEntry instanceEntry = new ParsedInstanceEntry(data.type(), field, null);
                    config.entries().put(data.id(), instanceEntry);
                    if (instanceEntry.defaultValue() == null) {
                        throw new IllegalArgumentException("Entry " + field.getName() + " must not have a null default value!");
                    }
                }
            }
            ConfigButton button = assertButton(field);
            if (button != null) {
                String lastEntry = config.entries().isEmpty() ? "" : config.entries().lastEntry().getKey();
                config.buttons().add(ParsedButton.of(field, lastEntry));
            }
        }

        for (Class<?> category : categories) {
            Category data = assertAnnotation(category, Category.class);
            config.categories().put(
                    data.value(),
                    populateEntries(
                            category,
                            new ParsedCategory(data, ParsedInfo.of(category, data.value()), config),
                            data.categories()
                    )
            );
        }

        return config;
    }

    private static void populateEntries(Object instance, ParsedObjectEntry entry) {
        assertValidClass(instance.getClass());
        for (Field field : instance.getClass().getDeclaredFields()) {
            ConfigEntry data = assertAcciessbleEntry(instance, field);
            if (data == null) continue;
            if (data.type() == EntryType.OBJECT) {
                throw new IllegalArgumentException("Entry " + field.getName() + " cannot be an object!");
            }
            ResourcefulConfigValueEntry valueEntry;
            if (field.getType() == Observable.class) {
                valueEntry = ParsedObservableEntry.of(data.type(), field, null);
            } else {
                valueEntry = new ParsedInstanceEntry(data.type(), field, instance);
            }

            if (valueEntry.defaultValue() == null) {
                throw new IllegalArgumentException("Entry " + field.getName() + " must not have a null default value!");
            }
            entry.entries().put(data.id(), valueEntry);
        }
    }

    private static ConfigButton assertButton(Field field) {
        ConfigButton data = field.getAnnotation(ConfigButton.class);
        if (data == null) return null;
        if (!Modifier.isPublic(field.getModifiers())) throw new IllegalArgumentException("Button " + field.getName() + " is not public!");
        if (!Modifier.isStatic(field.getModifiers())) throw new IllegalArgumentException("Button " + field.getName() + " is not static!");
        if (!Modifier.isFinal(field.getModifiers())) throw new IllegalArgumentException("Button " + field.getName() + " is not final!");
        if (field.getType() != Runnable.class) throw new IllegalArgumentException("Button " + field.getName() + " is not of type Runnable!");
        return data;
    }

    private static ConfigEntry assertEntry(Field field) {
        ConfigEntry data = assertAcciessbleEntry(null, field);
        String name = field.getName();
        if (data == null) return null;
        if (!Modifier.isStatic(field.getModifiers()))
            throw new IllegalArgumentException("Entry " + name + " is not static!");
        return data;
    }

    private static Class<?> getFieldType(Object instance, Field field, EntryType entry) {
        Class<?> type = field.getType();
        if (type.isArray()) {
            if (!entry.isAllowedInArrays()) {
                throw new IllegalArgumentException("Entry " + field.getName() + " is an array but its type is not allowed in arrays!");
            }
            type = type.getComponentType();
        } else if (type == Observable.class) {
            try {
                return ((Observable<?>) field.get(instance)).type();
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Entry " + field.getName() + " is not accessible!");
            }
        }
        return type;
    }

    private static ConfigEntry assertAcciessbleEntry(Object instance, Field field) {
        ConfigEntry data = field.getAnnotation(ConfigEntry.class);
        String name = field.getName();
        if (data == null) return null;
        if (!Modifier.isPublic(field.getModifiers()))
            throw new IllegalArgumentException("Entry " + name + " is not public!");
        if (data.type().mustBeFinal() && !Modifier.isFinal(field.getModifiers()))
            throw new IllegalArgumentException("Entry " + name + " must be final!");
        if (!data.type().mustBeFinal() && Modifier.isFinal(field.getModifiers()))
            throw new IllegalArgumentException("Entry " + name + " must not be final!");
        Class<?> type = getFieldType(instance, field, data.type());
        if (!data.type().test(type))
            throw new IllegalArgumentException("Entry " + field.getName() + " is not of type " + data.type().name() + "!");
        if (data.id().contains("."))
            throw new IllegalArgumentException("Entry " + field.getName() + " has an invalid id! Ids must not contain '.'");
        return data;
    }

    private static <T extends Annotation> T assertAnnotation(AnnotatedElement element, Class<T> annotation) {
        T instance = element.getAnnotation(annotation);
        if (instance == null) {
            throw new IllegalArgumentException("Class " + element + " is missing required annotation " + annotation.getName());
        }
        return instance;
    }

    private static void assertValidClass(Class<?> config) {
        if (!Modifier.isPublic(config.getModifiers()))
            throw new IllegalArgumentException("Config class must be public!");
        if (config.getEnclosingClass() != null && !Modifier.isStatic(config.getModifiers()))
            throw new IllegalArgumentException("Config class must be static!");
        if (config.isEnum()) throw new IllegalArgumentException("Config class cannot be an enum!");
        if (config.isInterface()) throw new IllegalArgumentException("Config class cannot be an interface!");
        if (config.isAnnotation()) throw new IllegalArgumentException("Config class cannot be an annotation!");
        if (config.isRecord()) throw new IllegalArgumentException("Config class cannot be a record!");
    }
}
