package com.teamresourceful.resourcefulconfig.common.config.fabric;

import com.google.gson.JsonObject;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.web.info.ResourcefulWebConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public class FabricResourcefulConfig implements ResourcefulConfig {

    private final ResourcefulWebConfig web;
    private final Map<String, FabricResourcefulConfigEntry> entries;
    private final Map<String, FabricResourcefulConfig> configs;
    private final String fileName;
    private final String translation;

    public FabricResourcefulConfig(ResourcefulWebConfig web, Map<String, FabricResourcefulConfigEntry> entries, Map<String, FabricResourcefulConfig> configs, String fileName, String translation) {
        this.web = web;
        this.entries = entries;
        this.configs = configs;
        this.fileName = fileName;
        this.translation = translation;
    }

    private File getConfigFile() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        File jsonFile = configDir.resolve(fileName + ".json").toFile();
        if (jsonFile.exists()) {
            return jsonFile;
        }
        return configDir.resolve(fileName + ".jsonc").toFile();
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
    public @NotNull ResourcefulWebConfig getWebConfig() {
        return web;
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
                JsoncObject json = new JsoncObject();
                FabricConfigLoader.saveConfig(this, json);
                FileUtils.write(getConfigFile(), json.toString(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.out.println("Failed to save config file " + fileName + ".json");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void load() {
        File file = getConfigFile();
        if (file.exists()) {
            try {
                String data = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                JsonObject json = JsoncObject.load(data);
                FabricConfigLoader.loadConfig(this, json);
            }catch (Exception e) {
                // NO-OP
            }
            if (file.getName().endsWith(".json") && !file.delete()) {
                System.out.println("Failed to delete old config file " + fileName + ".json");
            }
        }
    }
}
