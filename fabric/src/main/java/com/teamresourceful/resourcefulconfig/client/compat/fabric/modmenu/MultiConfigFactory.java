package com.teamresourceful.resourcefulconfig.client.compat.fabric.modmenu;

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public record MultiConfigFactory(@Nullable String modid) implements ConfigScreenFactory<Screen> {

    @Override
    public Screen create(Screen screen) {
        return ResourcefulConfigScreen.get(screen, modid);
    }
}
