package com.teamresourceful.resourcefulconfig.common.loader;

import com.teamresourceful.resourcefulconfig.api.config.EntryOptions;
import com.teamresourceful.resourcefulconfig.api.config.EntryType;
import com.teamresourceful.resourcefulconfig.api.config.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;

import java.lang.reflect.Field;

public record ParsedInstanceEntry(
    EntryType type,
    Field field,
    EntryOptions options,
    Object instance,
    Object defaultValue
) implements ResourcefulConfigValueEntry {

    public ParsedInstanceEntry(EntryType type, Field field, Object instance) {
        this(type, field, EntryOptions.of(field), instance, ParsingUtils.getField(field, instance));
    }

    @Override
    public Class<?> objectType() {
        return field.getType();
    }

    @Override
    public void reset() {
        if (field.getType().isArray()) {
            setArray((Object[]) defaultValue);
        } else {
            switch (type) {
                case BYTE -> setByte((byte) defaultValue);
                case SHORT -> setShort((short) defaultValue);
                case INTEGER -> setInt((int) defaultValue);
                case LONG -> setLong((long) defaultValue);
                case FLOAT -> setFloat((float) defaultValue);
                case DOUBLE -> setDouble((double) defaultValue);
                case BOOLEAN -> setBoolean((boolean) defaultValue);
                case STRING -> setString((String) defaultValue);
                case ENUM -> setEnum((Enum<?>) defaultValue);
                case OBJECT -> throw new IllegalStateException("Object cannot be in a value entry!");
            }
        }
    }
}
