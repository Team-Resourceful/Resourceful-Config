package com.teamresourceful.resourcefulconfig.common.config;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Contains all the configs that have been registered in any configurator.
 */
@ApiStatus.Internal
public record Configurations(Map<String, ResourcefulConfig> configs, Map<Class<?>, String> configClasses) implements Iterable<ResourcefulConfig> {

    public static final Configurations INSTANCE = new Configurations();

    private Configurations() {
        this(new HashMap<>(), new HashMap<>());
    }

    public void addConfig(ResourcefulConfig config) {
        configs.put(config.id(), config);
        configClasses.put(config.getClass(), config.id());
    }

    @NotNull
    @Override
    public Iterator<ResourcefulConfig> iterator() {
        return configs.values().iterator();
    }
}
