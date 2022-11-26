package com.teamresourceful.resourcefulconfig.common.config.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;

import java.util.Map;

public class ForgeResourcefulConfig implements ResourcefulConfig {

    private final Map<String, ForgeResourcefulConfigEntry> entries;
    private final Map<String, ForgeResourcefulConfig> configs;
    private final String fileName;
    private final String translation;
    @Nullable
    private final ForgeConfigSpec spec;

    public ForgeResourcefulConfig(Map<String, ForgeResourcefulConfigEntry> entries, Map<String, ForgeResourcefulConfig> configs, String fileName, String translation, @Nullable ForgeConfigSpec spec) {
        this.entries = entries;
        this.configs = configs;
        this.fileName = fileName;
        this.translation = translation;
        this.spec = spec;
    }

    @Override
    public Map<String, ? extends ResourcefulConfigEntry> getEntries() {
        return entries;
    }

    @Override
    public Map<String, ? extends ResourcefulConfig> getSubConfigs() {
        return configs;
    }

    @Override
    public @Nullable String getFileName() {
        return fileName;
    }

    @Override
    public String translation() {
        return translation;
    }

    @Override
    public void save() {
        entries.values().forEach(ForgeResourcefulConfigEntry::saveEntry);
        configs.values().forEach(ResourcefulConfig::save);
        if (spec != null) {
            spec.save();
        }
    }

    @Override
    public void load() {
        // NO-OP - Forge handles this for us every time the config is changed.
    }

    public ForgeConfigSpec getSpec() {
        if (spec == null) {
            throw new IllegalStateException("getSpec() called on a config that doesn't have a spec!");
        }
        return spec;
    }
}
