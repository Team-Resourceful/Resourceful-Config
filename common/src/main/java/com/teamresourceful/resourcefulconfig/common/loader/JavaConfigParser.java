package com.teamresourceful.resourcefulconfig.common.loader;

import com.teamresourceful.resourcefulconfig.api.annotations.*;
import com.teamresourceful.resourcefulconfig.api.loader.ConfigParser;
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
import java.util.ArrayList;
import java.util.List;

public class JavaConfigParser implements ConfigParser {

    @Override
    public int priority() {
        return Integer.MIN_VALUE;
    }

    @Override
    public ResourcefulConfig parse(Class<?> clazz) {
        Config data = assertAnnotation(clazz, Config.class);
        return populateEntries(clazz, new ParsedConfig(data, ParsedInfo.of(clazz, data.value())), data.categories());
    }

    private static <T extends ResourcefulConfig> T populateEntries(Class<?> clazz, T config, Class<?>[] categories) {
        assertValidClass(clazz);

        for (Field field : clazz.getDeclaredFields()) {
            ConfigEntry data = assertEntry(field);
            if (data != null) {
                var type = getEntryType(field);

                if (type == EntryType.OBJECT) {
                    Object instance = ParsingUtils.getField(field, null);
                    ParsedObjectEntry objectEntry = new ParsedObjectEntry(field);
                    populateEntries(instance, objectEntry);
                    config.entries().put(data.id(), objectEntry);
                } else if (field.getType() == Observable.class) {
                    ParsedObservableEntry observableEntry = ParsedObservableEntry.of(type, field, null);
                    config.entries().put(data.id(), observableEntry);
                    if (observableEntry.defaultValue() == null) {
                        throw new IllegalArgumentException("Entry " + field.getName() + " must not have a null default value!");
                    }
                } else {
                    ParsedInstanceEntry instanceEntry = new ParsedInstanceEntry(type, field, null);
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
            ConfigEntry data = assertAccessibleEntry(instance, field);
            if (data == null) continue;
            EntryType type = getEntryType(field);
            if (type == EntryType.OBJECT) {
                throw new IllegalArgumentException("Entry " + field.getName() + " cannot be an object!");
            }
            ResourcefulConfigValueEntry valueEntry;
            if (field.getType() == Observable.class) {
                valueEntry = ParsedObservableEntry.of(type, field, null);
            } else {
                valueEntry = new ParsedInstanceEntry(type, field, instance);
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
        ConfigEntry data = assertAccessibleEntry(null, field);
        String name = field.getName();
        if (data == null) return null;
        if (!Modifier.isStatic(field.getModifiers()))
            throw new IllegalArgumentException("Entry " + name + " is not static!");
        return data;
    }

    private static Class<?> getFieldType(Object instance, Field field, EntryType entry) {
        Class<?> type = field.getType();
        if (type == Observable.class) {
            try {
                type = ((Observable<?>) field.get(instance)).type();
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Entry " + field.getName() + " is not accessible!");
            }
        }

        if (type.isArray()) {
            if (!entry.isAllowedInArrays()) {
                throw new IllegalArgumentException("Entry " + field.getName() + " is an array but its type is not allowed in arrays!");
            }
            type = type.getComponentType();
        }
        return type;
    }

    private static ConfigEntry assertAccessibleEntry(Object instance, Field field) {
        ConfigEntry data = field.getAnnotation(ConfigEntry.class);
        String name = field.getName();
        if (data == null) return null;
        EntryType type = getEntryType(field);
        List<String> errors = new ArrayList<>();
        if (!Modifier.isPublic(field.getModifiers())) errors.add("Entry is not public!");
        if (type.mustBeFinal() && !Modifier.isFinal(field.getModifiers())) errors.add("Entry must be final!");
        if (!type.mustBeFinal() && Modifier.isFinal(field.getModifiers())) errors.add("Entry must not be final!");
        if (!type.test(getFieldType(instance, field, type))) errors.add("Entry has an invalid type for " + type + "!");
        if (data.id().contains(".")) errors.add(data.id() + " is an invalid id!");
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(
                    "Entry " + name + " is invalid!\n\t" + String.join("\n\t", errors) + "\n"
            );
        }
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
        List<String> errors = new ArrayList<>();
        if (!Modifier.isPublic(config.getModifiers())) errors.add("Config class must be public!");
        if (config.getEnclosingClass() != null && !Modifier.isStatic(config.getModifiers())) errors.add("Inner config class must be static!");
        if (config.isEnum()) errors.add("Config class cannot be an enum!");
        if (config.isInterface()) errors.add("Config class cannot be an interface!");
        if (config.isAnnotation()) errors.add("Config class cannot be an annotation!");
        if (config.isRecord()) errors.add("Config class cannot be a record!");

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(
                    "Config class " + config.getName() + " is invalid!\n\t" + String.join("\n\t", errors) + "\n"
            );
        }
    }

    private static EntryType getEntryType(Field field) {
        Class<?> fieldType = field.getType();
        if (fieldType == Observable.class) fieldType = ((Observable<?>) ParsingUtils.getField(field, null)).type();
        if (fieldType.isArray()) fieldType = fieldType.getComponentType();
        return getEntryType(fieldType);
    }

    private static EntryType getEntryType(Class<?> type) {
        if (type.getAnnotation(ConfigObject.class) != null) return EntryType.OBJECT;
        if (type == Long.TYPE || type == Long.class) return EntryType.LONG;
        if (type == Integer.TYPE || type == Integer.class) return EntryType.INTEGER;
        if (type == Short.TYPE || type == Short.class) return EntryType.SHORT;
        if (type == Byte.TYPE || type == Byte.class) return EntryType.BYTE;
        if (type == Double.TYPE || type == Double.class) return EntryType.DOUBLE;
        if (type == Float.TYPE || type == Float.class) return EntryType.FLOAT;
        if (type == Boolean.TYPE || type == Boolean.class) return EntryType.BOOLEAN;
        if (type == String.class) return EntryType.STRING;
        if (type.isEnum()) return EntryType.ENUM;
        throw new IllegalArgumentException("Entry " + type + " is not a valid type!");
    }
}
