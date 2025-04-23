package com.teamresourceful.resourcefulconfig.api.client;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface ResourcefulConfigElementRenderer {

    Component title();

    Component description();

    List<AbstractWidget> widgets();

    @FunctionalInterface
    interface Factory {
        ResourcefulConfigElementRenderer create(ResourcefulConfigElement context);
    }
}
