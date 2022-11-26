package com.teamresourceful.resourcefulconfig.common.config;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.apache.commons.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class Configurator {

    private final Map<String, ResourcefulConfig> configs = new HashMap<>();
    private final Map<Class<?>, String> configClasses = new HashMap<>();

    private final ConfigLoader creator;

    public Configurator() {
        this.creator = getCreator();
    }

    public void registerConfig(Class<?> configClass) {
        var config = creator.registerConfig(configClass);
        if (config != null) {
            configs.put(config.getFileName(), config);
            configClasses.put(configClass, config.getFileName());
        }
    }

    public boolean saveConfig(Class<?> config) {
        if (configClasses.containsKey(config)) {
            return saveConfig(configClasses.get(config));
        }
        return false;
    }

    public boolean saveConfig(String fileName) {
        var config = getConfig(fileName);
        if (config != null) {
            config.save();
            return true;
        }
        return false;
    }

    public boolean loadConfig(Class<?> config) {
        if (configClasses.containsKey(config)) {
            return loadConfig(configClasses.get(config));
        }
        return false;
    }

    public boolean loadConfig(String fileName) {
        var config = getConfig(fileName);
        if (config != null) {
            config.load();
            return true;
        }
        return false;
    }

    public ResourcefulConfig getConfig(String fileName) {
        return configs.get(fileName);
    }

    public ResourcefulConfig getConfig(Class<?> config) {
        if (configClasses.containsKey(config)) {
            return getConfig(configClasses.get(config));
        }
        return null;
    }

    @ExpectPlatform
    public static ConfigLoader getCreator() {
        throw new NotImplementedException();
    }
}
