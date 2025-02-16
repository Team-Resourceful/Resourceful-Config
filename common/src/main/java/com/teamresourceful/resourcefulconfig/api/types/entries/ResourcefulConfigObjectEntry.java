package com.teamresourceful.resourcefulconfig.api.types.entries;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public interface ResourcefulConfigObjectEntry extends ResourcefulConfigEntry {

    @NotNull
    LinkedHashMap<String, ResourcefulConfigEntry> entries();

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
