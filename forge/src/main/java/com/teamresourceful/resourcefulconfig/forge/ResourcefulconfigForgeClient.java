package com.teamresourceful.resourcefulconfig.forge;

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

public class ResourcefulconfigForgeClient {

    public static void onClientInit(ModContainer container) {
        container.registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory(
                (client, parent) -> ResourcefulConfigScreen.getFactory(null).apply(parent)
            )
        );
    }

    public static void onClientComplete() {
        for (var modId : Configurations.INSTANCE.getModIds()) {
            ModContainer container = ModList.get().getModContainerById(modId).orElse(null);
            if (container == null) {
                ModUtils.log("Could not find mod container for mod id '" + modId + "'. Skipping config generation.");
                continue;
            }
            if (container.getCustomExtension(ConfigScreenHandler.ConfigScreenFactory.class).isPresent()) {
                ModUtils.debug("Mod '" + modId + "' already has a config screen factory. Skipping config generation.");
                continue;
            }
            container.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                    (client, parent) -> ResourcefulConfigScreen.getFactory(modId).apply(parent)
                )
            );
        }
    }
}