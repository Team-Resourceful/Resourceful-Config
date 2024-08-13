package com.teamresourceful.resourcefulconfig.common.config;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains all the configs that have been registered in any configurator.
 */
@ApiStatus.Internal
public record Configurations(
        Map<String, Set<String>> modToConfigs,
        Map<String, ResourcefulConfig> configs
) implements Iterable<ResourcefulConfig> {

    public static final Configurations INSTANCE = new Configurations();

    private Configurations() {
        this(new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
    }

    private void addModConfigs(String modid, String configId) {
        modToConfigs.computeIfAbsent(modid, s -> new HashSet<>()).add(configId);
    }

    public void addConfig(ResourcefulConfig config, String modid) {
        if (modid != null) addModConfigs(modid, config.id());
        configs.put(config.id(), config);
    }

    @NotNull
    @Override
    public Iterator<ResourcefulConfig> iterator() {
        return configs.values().iterator();
    }

    public ResourcefulConfig getConfig(String id) {
        return configs.get(id);
    }

    public Set<String> getConfigsForMod(String modid) {
        if (modid == null) return Set.of();
        return modToConfigs.getOrDefault(modid, Set.of());
    }

    public Collection<String> getModIds() {
        return modToConfigs.keySet();
    }
}
