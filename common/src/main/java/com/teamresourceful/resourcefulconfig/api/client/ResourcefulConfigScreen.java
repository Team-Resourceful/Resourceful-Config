package com.teamresourceful.resourcefulconfig.api.client;

import com.teamresourceful.resourcefulconfig.api.loader.Configurator;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.client.ConfigsScreen;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import net.minecraft.Optionull;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class ResourcefulConfigScreen {

    /**
     * Gets a screen for the given config.
     */
    public static Screen get(@Nullable Screen parent, Configurator configurator, Class<?> clazz) {
        ResourcefulConfig config = configurator.getConfig(clazz);
        return Optionull.map(config, c -> get(parent, c));
    }

    /**
     * Gets a screen for the given config.
     */
    public static Screen get(@Nullable Screen parent, ResourcefulConfig config) {
        return new ConfigScreen(parent, config);
    }

    public static Screen get(@Nullable Screen parent, String mod) {
        return new ConfigsScreen(parent, mod);
    }

    public static Function<@Nullable Screen, Screen> getFactory(String mod) {
        List<String> configs = Configurations.INSTANCE.getConfigsForMod(mod);
        if (configs.size() == 1) {
            return screen -> get(screen, configs.get(0));
        } else {
            return screen -> get(screen, mod);
        }
    }

    /**
     * Opens a modal screen with the given title and constructor.
     * @param title The title of the modal screen.
     * @param constructor The constructor for the modal screen. This should be a lambda that returns a widget that contains the modal screen's content.
     *
     * @apiNote The widget for this modal should contain all other widgets and should be used for rendering of inside the modal.
     */
    public static void openModal(Component title, ModalWidgetConstructor constructor) {
        new GenericModalOverlay(title, constructor).open();
    }
}
