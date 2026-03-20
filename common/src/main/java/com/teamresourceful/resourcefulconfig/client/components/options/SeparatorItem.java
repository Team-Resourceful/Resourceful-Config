package com.teamresourceful.resourcefulconfig.client.components.options;

import com.teamresourceful.resourcefulconfig.client.UIConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SeparatorItem extends OptionItem {

    public SeparatorItem(Component title, Component description) {
        super(title, description, List.of());
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, partialTicks);
        graphics.fill(this.getX() + PADDING, this.getBottom() - 3, this.getRight() - PADDING, this.getBottom() - 2, UIConstants.TEXT_PARAGRAPH);
    }
}
