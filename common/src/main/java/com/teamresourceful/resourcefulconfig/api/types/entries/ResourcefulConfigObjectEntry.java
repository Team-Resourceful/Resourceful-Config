package com.teamresourceful.resourcefulconfig.api.types.entries;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;

public interface ResourcefulConfigObjectEntry extends ResourcefulConfigEntry {

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
    default List<ResourcefulConfigElement> elements() {
        return List.of();
    }

    /**
     * @deprecated This method was only ever intended for grabbing information like title of the object nothing else, this was an oversight to include it.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "22")
    default Object instance() {
        return null;
    }

    default Component getTitle(@NotNull Component fallback) {
        return fallback;
    }

}
