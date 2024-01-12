package com.teamresourceful.resourcefulconfig.api.loader;


import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import com.teamresourceful.resourcefulconfig.common.loader.Parser;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;

public final class Configurator {

    private final Map<String, ResourcefulConfig> configs = new HashMap<>();
    private final Map<Class<?>, String> configClasses = new HashMap<>();

    private final String modid;

    public Configurator(String modid) {
        this.modid = modid;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20.5")
    public Configurator() {
        this(null);
    }

    public void register(Class<?> clazz) {
        var config = registerConfig(clazz);
        if (config != null) {
            configClasses.put(clazz, config.id());
            register(config);
        }
    }

    public void register(ResourcefulConfig config) {
        configs.put(config.id(), config);
        Configurations.INSTANCE.addConfig(config, modid);
    }

    @ApiStatus.Internal
    private ResourcefulConfig registerConfig(Class<?> clazz) {
        try {
            ResourcefulConfig config = Parser.parse(clazz);
            config.load();
            config.save();
            return config;
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to create config for " + clazz.getName());
        }
        return null;
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
