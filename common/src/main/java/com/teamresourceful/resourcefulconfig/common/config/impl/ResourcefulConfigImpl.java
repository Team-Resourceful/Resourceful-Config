package com.teamresourceful.resourcefulconfig.common.config.impl;

import com.google.gson.JsonObject;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigButton;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.common.jsonc.JsoncObject;
import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;
import com.teamresourceful.resourcefulconfig.web.info.ResourcefulWebConfig;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ResourcefulConfigImpl implements ResourcefulConfig {

    private final ResourcefulWebConfig web;
    private final Map<String, ResourcefulConfigEntryImpl> entries;
    private final List<ResourcefulConfigButton> buttons;
    private final Map<String, ResourcefulConfigImpl> configs;
    private final String fileName;
    private final String translation;

    public ResourcefulConfigImpl(ResourcefulWebConfig web, Map<String, ResourcefulConfigEntryImpl> entries, List<ResourcefulConfigButton> buttons, Map<String, ResourcefulConfigImpl> configs, String fileName, String translation) {
        this.web = web;
        this.entries = entries;
        this.buttons = buttons;
        this.configs = configs;
        this.fileName = fileName;
        this.translation = translation;
    }

    private File getConfigFile() {
        Path configDir = ModUtils.getConfigPath();
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
    public List<? extends ResourcefulConfigButton> getButtons() {
        return buttons;
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
                ConfigLoaderImpl.saveConfig(this, json);
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
                JsonObject json = JsoncObject.parse(data);
                ConfigLoaderImpl.loadConfig(this, json);
            }catch (Exception e) {
                // NO-OP
            }
            if (file.getName().endsWith(".json") && !file.delete()) {
                System.out.println("Failed to delete old config file " + fileName + ".json");
            }
        }
    }
}
