package com.teamresourceful.resourcefulconfig.api.config;

import com.teamresourceful.resourcefulconfig.web.info.ResourcefulWebConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;

public interface ResourcefulConfig {

    @NotNull
    LinkedHashMap<String, ResourcefulConfigEntry> entries();

    @NotNull
    LinkedHashMap<String, ResourcefulConfig> categories();

    @NotNull
    List<? extends ResourcefulConfigButton> buttons();

    @NotNull
    ResourcefulWebConfig webConfig();

    String id();

    String translation();

    void save();

    void load();

    boolean hasFile();

    default MutableComponent toComponent() {
        return Component.translatableWithFallback(translation(), id());
    }

}
