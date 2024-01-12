package com.teamresourceful.resourcefulconfig.common.loader;

import com.google.gson.JsonObject;
import com.teamresourceful.resourcefulconfig.api.annotations.Config;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.common.jsonc.JsoncObject;
import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public record ParsedConfig(
    @NotNull String id,
    @NotNull String translation,
    @NotNull ResourcefulConfigInfo info,
    @NotNull LinkedHashMap<String, ResourcefulConfigEntry> entries,
    @NotNull LinkedHashMap<String, ResourcefulConfig> categories,
    @NotNull List<ResourcefulConfigButton> buttons
) implements ResourcefulConfig {

    public ParsedConfig(Config config, ResourcefulConfigInfo info) {
        this(config.value(), config.translation(), info, new LinkedHashMap<>(), new LinkedHashMap<>(), new ArrayList<>());
    }

    private File getConfigFile() {
        Path configDir = ModUtils.getConfigPath();
        File jsonFile = configDir.resolve(id + ".json").toFile();
        if (jsonFile.exists()) {
            return jsonFile;
        }
        return configDir.resolve(id + ".jsonc").toFile();
    }

    @Override
    public void save() {
        try {
            FileUtils.write(getConfigFile(), Writer.save(this).toString(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            ModUtils.log("Failed to save config file " + id + ".json", e);
        }
    }

    @Override
    public void load() {
        File file = getConfigFile();
        if (file.exists()) {
            try {
                String data = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                JsonObject json = JsoncObject.parse(data);
                Loader.loadConfig(this, json);
            }catch (Exception e) {
                // NO-OP
            }
            if (file.getName().endsWith(".json") && !file.delete()) {
                ModUtils.log("Failed to delete old config file " + id + ".json");
            }
        }
    }

    @Override
    public boolean hasFile() {
        return true;
    }
}
