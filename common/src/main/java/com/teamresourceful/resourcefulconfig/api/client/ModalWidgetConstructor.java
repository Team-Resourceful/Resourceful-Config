package com.teamresourceful.resourcefulconfig.api.client;

import net.minecraft.client.gui.components.AbstractWidget;

@FunctionalInterface
public interface ModalWidgetConstructor {

    AbstractWidget construct(int x, int y, int width, int height);
}
