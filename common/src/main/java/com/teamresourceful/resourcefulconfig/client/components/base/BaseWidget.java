package com.teamresourceful.resourcefulconfig.client.components.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;

public abstract class BaseWidget extends AbstractWidget {

    protected final Font font;
    protected final Minecraft minecraft;

    public BaseWidget(int width, int height) {
        super(0, 0, width, height, CommonComponents.EMPTY);
        this.minecraft = Minecraft.getInstance();
        this.font = this.minecraft.font;
    }

    @Override
    protected abstract void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks);

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
