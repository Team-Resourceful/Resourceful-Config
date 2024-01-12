package com.teamresourceful.resourcefulconfig.api.types;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigInfo;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.common.info.ParsedInfo;
import com.teamresourceful.resourcefulconfig.web.info.ResourcefulWebConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.ApiStatus;
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

    /**
     * @deprecated Use {@link #info()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20.5")
    @NotNull
    default ResourcefulWebConfig webConfig() {
        return ResourcefulWebConfig.DEFAULT;
    }

    /**
     * @apiNote This method is only default until 1.20.5 when it will no longer be default.
     */
    @NotNull
    default ResourcefulConfigInfo info() {
        return ParsedInfo.EMPTY;
    }

    String id();

    /**
     * @deprecated Use {@link ConfigInfo#titleTranslation()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20.5")
    String translation();

    void save();

    void load();

    boolean hasFile();

    /**
     * @deprecated Use {@link ConfigInfo#title()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20.5")
    default MutableComponent toComponent() {
        if (info().title().hasTranslation()) {
            return info().title().toComponent();
        }
        return Component.translatableWithFallback(translation(), id());
    }

}
