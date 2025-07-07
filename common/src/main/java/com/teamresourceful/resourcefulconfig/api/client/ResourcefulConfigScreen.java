package com.teamresourceful.resourcefulconfig.api.client;

import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.client.ConfigScreenContext;
import com.teamresourceful.resourcefulconfig.client.ConfigsScreen;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import net.minecraft.Optionull;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class ResourcefulConfigScreen {

    public static ResourcefulConfigScreenBuilder make(ResourcefulConfig config) {
        return new ResourcefulConfigScreenBuilder(config);
    }

    public static ResourcefulConfigScreenBuilder make(Configurator configurator, Class<?> clazz) {
        var config = Objects.requireNonNull(
                configurator.getConfig(clazz),
                "Config not found for class: " + clazz.getName()
        );
        return make(config);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = ">1.21.7")
    public static Screen get(@Nullable Screen parent, Configurator configurator, Class<?> clazz) {
        return Optionull.map(configurator.getConfig(clazz), c -> get(parent, c));
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = ">1.21.7")
    public static Screen get(@Nullable Screen parent, ResourcefulConfig config) {
        return new ConfigScreen(parent, config, new ConfigScreenContext());
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = ">1.21.7")
    public static Screen get(@Nullable Screen parent, ResourcefulConfig config, Function<String, List<String>> termCollector) {
        return new ConfigScreen(parent, config, new ConfigScreenContext("", termCollector));
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = ">1.21.7")
    public static Screen get(@Nullable Screen parent, String mod) {
        return new ConfigsScreen(parent, mod);
    }

    public static Function<@Nullable Screen, Screen> getFactory(String mod) {
        Set<String> configs = Configurations.INSTANCE.getConfigsForMod(mod);
        if (configs.size() != 1) {
            var nonHiddenCount = configs.stream()
                    .map(Configurations.INSTANCE::getConfig)
                    .filter(it -> !it.info().isHidden())
                    .count();

            if (nonHiddenCount != 1) return screen -> new ConfigsScreen(screen, mod);
        }

        if (configs.isEmpty()) return screen -> new ConfigsScreen(screen, mod);
        ResourcefulConfig config = Configurations.INSTANCE.getConfig(configs.iterator().next());
        return config == null ? Function.identity() : screen -> make(config).withParent(screen).build();
    }
}
