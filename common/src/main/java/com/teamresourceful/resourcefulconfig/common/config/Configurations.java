package com.teamresourceful.resourcefulconfig.common.config;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Contains all the configs that have been registered in any configurator.
 */
@ApiStatus.Internal
public record Configurations(
        Map<String, List<String>> modToConfigs,
        Map<String, ResourcefulConfig> configs
) implements Iterable<ResourcefulConfig> {

    public static final Configurations INSTANCE = new Configurations();

    private Configurations() {
        this(new HashMap<>(), new HashMap<>());
    }

    private void addModConfigs(String modid, String configId) {
        modToConfigs.computeIfAbsent(modid, s -> new ArrayList<>()).add(configId);
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

    public List<String> getConfigsForMod(String modid) {
        return modToConfigs.getOrDefault(modid, List.of());
    }

    public Collection<String> getModIds() {
        return modToConfigs.keySet();
    }
}
