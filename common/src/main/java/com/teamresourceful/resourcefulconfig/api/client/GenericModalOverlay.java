package com.teamresourceful.resourcefulconfig.api.client;

import com.teamresourceful.resourcefulconfig.client.screens.base.ModalOverlay;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class GenericModalOverlay extends ModalOverlay {

    private final ModalWidgetConstructor constructor;

    public GenericModalOverlay(Component title, ModalWidgetConstructor constructor) {
        super();
        this.title = title;
        this.constructor = constructor;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(constructor.construct(left, top, contentWidth, contentHeight));
    }
}
