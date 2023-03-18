package com.teamresourceful.resourcefulconfig.common.config;

import com.teamresourceful.resourcefulconfig.web.info.ResourcefulWebConfig;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public interface ResourcefulConfig {

    Map<String, ? extends ResourcefulConfigEntry> getEntries();
    Map<String, ? extends ResourcefulConfig> getSubConfigs();

    @NotNull
    ResourcefulWebConfig getWebConfig();

    /**
     * @return The file name of this config. Null if this is a sub config.
     */
    @Nullable
    String getFileName();

    String translation();

    default Component getDisplayName() {
        return Component.translatable(translation());
    }

    void save();

    void load();

    default Optional<? extends ResourcefulConfigEntry> getEntry(String id) {
        return Optional.ofNullable(getEntries().get(id));
    }

    default Optional<? extends ResourcefulConfig> getSubConfig(String id) {
        return Optional.ofNullable(getSubConfigs().get(id));
    }
}
