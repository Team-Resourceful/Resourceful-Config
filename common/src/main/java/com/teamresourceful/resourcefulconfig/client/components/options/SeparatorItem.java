package com.teamresourceful.resourcefulconfig.client.components.options;

import com.teamresourceful.resourcefulconfig.client.UIConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SeparatorItem extends OptionItem {

    public SeparatorItem(Component title, Component description) {
        super(title, description, List.of());
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        graphics.fill(this.getX() + PADDING, this.getBottom() - 3, this.getRight() - PADDING, this.getBottom() - 2, UIConstants.TEXT_PARAGRAPH);
    }
}
