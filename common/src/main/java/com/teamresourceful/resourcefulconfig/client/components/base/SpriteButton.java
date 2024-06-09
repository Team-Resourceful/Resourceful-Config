package com.teamresourceful.resourcefulconfig.client.components.base;

import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SpriteButton extends AbstractButton {

    protected final int padding;
    protected final ResourceLocation sprite;
    protected final Runnable onPress;

    protected SpriteButton(int width, int height, int padding, ResourceLocation sprite, Runnable onPress, @Nullable Component tooltip) {
        super(0, 0, width + padding * 2, height + padding * 2, tooltip == null ? CommonComponents.EMPTY : tooltip);
        this.padding = padding;
        this.sprite = sprite;
        this.onPress = onPress;
        setTooltip(Tooltip.create(getMessage()));
    }

    public static Builder builder(int width, int height) {
        return new Builder(width, height);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        ResourceLocation button = isHovered() ? ModSprites.BUTTON_HOVER : ModSprites.BUTTON;
        graphics.blitSprite(button, getX(), getY(), getWidth(), getHeight());
        graphics.blitSprite(
            this.sprite,
            getX() + this.padding, getY() + this.padding,
            getWidth() - this.padding * 2, getHeight() - this.padding * 2
        );
    }

    @Override
    public void onPress() {
        this.onPress.run();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public static class Builder {

            private final int width;
            private final int height;
            private int padding;
            private ResourceLocation sprite;
            private Runnable onPress = () -> {};
            private Component tooltip = null;

            public Builder(int width, int height) {
                this.width = width;
                this.height = height;
            }

            public Builder padding(int padding) {
                this.padding = padding;
                return this;
            }

            public Builder sprite(ResourceLocation sprite) {
                this.sprite = sprite;
                return this;
            }

            public Builder onPress(Runnable onPress) {
                this.onPress = onPress;
                return this;
            }

            public Builder tooltip(Component tooltip) {
                this.tooltip = tooltip;
                return this;
            }

            public SpriteButton build() {
                return new SpriteButton(width, height, padding, sprite, onPress, tooltip);
            }
    }
}
