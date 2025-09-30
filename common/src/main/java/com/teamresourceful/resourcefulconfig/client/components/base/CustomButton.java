package com.teamresourceful.resourcefulconfig.client.components.base;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

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
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, button, getX(), getY(), getWidth(), getHeight());
        renderScrollingString(
            graphics, Minecraft.getInstance().font,
            this.text,
            getX() + 2, getY() + 2, getX() + getWidth() - 2, getY() + getHeight() - 2,
            UIConstants.TEXT_TITLE
        );

        if (this.isHovered()) {
            graphics.requestCursor(this.isActive() ? CursorTypes.POINTING_HAND : CursorTypes.NOT_ALLOWED);
        }
    }

    @Override
    public void onPress(@NotNull InputWithModifiers modifiers) {
        this.onPress.run();
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {

    }
}
