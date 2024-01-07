package com.teamresourceful.resourcefulconfig.client.components.base;

import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CustomButton extends AbstractButton {

    private final Component text;
    private final Runnable onPress;

    public CustomButton(int width, int height, Component text, Runnable onPress) {
        super(0, 0, width + 4, height + 4, CommonComponents.EMPTY);
        this.text = text;
        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        ResourceLocation button = isHovered() ? ModSprites.BUTTON_HOVER : ModSprites.BUTTON;
        graphics.blitSprite(button, getX(), getY(), getWidth(), getHeight());
        renderScrollingString(
            graphics, Minecraft.getInstance().font,
            this.text,
            getX() + 2, getY() + 2, getX() + getWidth() - 2, getY() + getHeight() - 2,
            UIConstants.TEXT_TITLE
        );

    }

    @Override
    public void onPress() {
        this.onPress.run();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
