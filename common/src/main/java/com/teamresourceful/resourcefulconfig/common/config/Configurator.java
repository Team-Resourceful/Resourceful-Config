package com.teamresourceful.resourcefulconfig.common.config;

import com.teamresourceful.resourcefulconfig.common.config.impl.ConfigLoaderImpl;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

public class Configurator {

    private final Map<String, ResourcefulConfig> configs = new HashMap<>();
    private final Map<Class<?>, String> configClasses = new HashMap<>();

    private final ConfigLoader creator = new ConfigLoaderImpl();

    public Configurator() {}

    /**
     * @deprecated Use {@link #Configurator()} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20.1")
    public Configurator(boolean ignored) {
        this();
    }

    public void registerConfig(Class<?> configClass) {
        var config = creator.registerConfig(configClass);
        if (config != null) {
            configs.put(config.getFileName(), config);
            configClasses.put(configClass, config.getFileName());
            Configurations.INSTANCE.addConfig(config);
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
}
