package com.teamresourceful.resourcefulconfig.api.types;

import com.teamresourceful.resourcefulconfig.api.patching.ConfigPatchEvent;
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

public interface ResourcefulConfig {

    default int version() {
        return 0;
    }

    @NotNull
    default List<ResourcefulConfigElement> elements() {
        return List.of();
    }

    @NotNull
    LinkedHashMap<String, ResourcefulConfig> categories();

    @NotNull
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = ">1.21.5")
    default LinkedHashMap<String, ResourcefulConfigEntry> entries() {
        LinkedHashMap<String, ResourcefulConfigEntry> entries = new LinkedHashMap<>();
        for (ResourcefulConfigElement element : elements()) {
            if (element instanceof ResourcefulConfigEntryElement entry) {
                entries.put(entry.id(), entry.entry());
            }
        }
        return entries;
    }

    @NotNull
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = ">1.21.5")
    default List<ResourcefulConfigButton> buttons() {
        List<ResourcefulConfigButton> buttons = new ArrayList<>();
        for (ResourcefulConfigElement element : elements()) {
            if (element instanceof ResourcefulConfigButton button) {
                buttons.add(button);
            }
        }
        return buttons;
    }

    @NotNull
    ResourcefulConfigInfo info();

    String id();

    void save();

    void load(Consumer<ConfigPatchEvent> handler);

}
