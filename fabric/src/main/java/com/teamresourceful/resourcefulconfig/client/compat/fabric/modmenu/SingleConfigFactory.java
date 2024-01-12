package com.teamresourceful.resourcefulconfig.client.compat.fabric.modmenu;

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.minecraft.client.gui.screens.Screen;

public record SingleConfigFactory(ResourcefulConfig config) implements ConfigScreenFactory<Screen> {

    @Override
    public Screen create(Screen screen) {
        return ResourcefulConfigScreen.get(screen, config);
    }
}
