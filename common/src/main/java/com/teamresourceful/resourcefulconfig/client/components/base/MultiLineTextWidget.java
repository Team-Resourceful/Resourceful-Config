package com.teamresourceful.resourcefulconfig.client.components.base;

import net.minecraft.client.gui.Font;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class MultiLineTextWidget extends net.minecraft.client.gui.components.MultiLineTextWidget {
    public MultiLineTextWidget(Component component, Font font) {
        super(component, font);

        this.active = true;
    }

    @Override
    public void playDownSound(@NotNull SoundManager manager) {

    }
}
