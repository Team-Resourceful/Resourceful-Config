package com.teamresourceful.resourcefulconfig.common.config.impl;

import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;

import java.lang.reflect.Field;

public record ResourcefulConfigEntryImpl(EntryType type, Field field, Object defaultValue) implements ResourcefulConfigEntry {

    public static ResourcefulConfigEntryImpl create(ConfigEntry entry, Field field) {
        Object value = ParsingUtils.getField(field);
        return new ResourcefulConfigEntryImpl(entry.type(), field, value);
    }
}
