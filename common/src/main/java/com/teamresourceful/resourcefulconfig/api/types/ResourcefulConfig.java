package com.teamresourceful.resourcefulconfig.api.types;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;

public interface ResourcefulConfig {

    @NotNull
    LinkedHashMap<String, ResourcefulConfigEntry> entries();

    @NotNull
    LinkedHashMap<String, ResourcefulConfig> categories();

    @NotNull
    List<ResourcefulConfigButton> buttons();

    @NotNull
    ResourcefulConfigInfo info();

    String id();

    void save();

    void load();

    boolean hasFile();

}
