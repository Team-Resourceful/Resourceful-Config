package com.teamresourceful.resourcefulconfig.common.config.fabric;

import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;

import java.lang.reflect.Field;

public class FabricResourcefulConfigEntry implements ResourcefulConfigEntry {

    private final EntryType type;
    private final Field field;
    private final Object defaultValue;

    private FabricResourcefulConfigEntry(EntryType type, Field field, Object defaultValue) {
        this.type = type;
        this.field = field;
        this.defaultValue = defaultValue;
    }

    public static FabricResourcefulConfigEntry create(ConfigEntry entry, Field field) {
        Object value = ParsingUtils.getField(field);
        return new FabricResourcefulConfigEntry(entry.type(), field, value);
    }

    @Override
    public EntryType type() {
        return type;
    }

    @Override
    public Field field() {
        return field;
    }

    @Override
    public Object defaultValue() {
        return defaultValue;
    }
}
