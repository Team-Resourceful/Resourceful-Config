package com.teamresourceful.resourcefulconfig.common.loader;

import com.teamresourceful.resourcefulconfig.api.config.EntryOptions;
import com.teamresourceful.resourcefulconfig.api.config.EntryType;
import com.teamresourceful.resourcefulconfig.api.config.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.config.ResourcefulConfigObjectEntry;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public record ParsedObjectEntry(
        EntryType type,
        Field field,
        EntryOptions options,
        LinkedHashMap<String, ResourcefulConfigEntry> entries
) implements ResourcefulConfigObjectEntry {

    public ParsedObjectEntry(EntryType type, Field field) {
        this(type, field, EntryOptions.of(field), new LinkedHashMap<>());
    }

    @Override
    public void reset() {
        entries.values().forEach(ResourcefulConfigEntry::reset);
    }
}
