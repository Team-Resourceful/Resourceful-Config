package com.teamresourceful.resourcefulconfig.api.types.entries;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public interface ResourcefulConfigObjectEntry extends ResourcefulConfigEntry {

    @NotNull
    LinkedHashMap<String, ResourcefulConfigEntry> entries();

    default Object instance() {
        return null;
    }

}
