package com.teamresourceful.resourcefulconfig.api.types.entries;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ResourcefulConfigObjectEntry extends ResourcefulConfigEntry {

    @NotNull
    default List<ResourcefulConfigElement> elements() {
        return List.of();
    }

    default Component getTitle(@NotNull Component fallback) {
        return fallback;
    }

}
