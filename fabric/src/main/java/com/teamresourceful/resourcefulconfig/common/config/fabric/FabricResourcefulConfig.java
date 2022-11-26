package com.teamresourceful.resourcefulconfig.common.config.fabric;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public class FabricResourcefulConfig implements ResourcefulConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<String, FabricResourcefulConfigEntry> entries;
    private final Map<String, FabricResourcefulConfig> configs;
    private final String fileName;
    private final String translation;

    public FabricResourcefulConfig(Map<String, FabricResourcefulConfigEntry> entries, Map<String, FabricResourcefulConfig> configs, String fileName, String translation) {
        this.entries = entries;
        this.configs = configs;
        this.fileName = fileName;
        this.translation = translation;
    }

    private File getConfigFile() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        return configDir.resolve(fileName + ".json").toFile();
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
        if (fileName != null) {
            try {
                JsonObject json = new JsonObject();
                FabricConfigLoader.saveConfig(this, json);
                FileUtils.write(getConfigFile(), GSON.toJson(json), StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.out.println("Failed to save config file " + fileName + ".json");
            }
        }
    }

    @Override
    public void load() {
        File file = getConfigFile();
        if (file.exists()) {
            try {
                String data = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                JsonObject json = GSON.fromJson(data, JsonObject.class);
                FabricConfigLoader.loadConfig(this, json);
            }catch (Exception e) {
                // NO-OP
            }
        }
    }
}
