package com.teamresourceful.resourcefulconfig.client.components.base;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpriteButton extends AbstractButton {

    protected final int padding;
    protected final Identifier sprite;
    protected final Runnable onPress;

    protected SpriteButton(int width, int height, int padding, Identifier sprite, Runnable onPress, @Nullable Component tooltip) {
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
    protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Identifier button = this.isHovered() && this.isActive() ? ModSprites.BUTTON_HOVER : ModSprites.BUTTON;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, button, getX(), getY(), getWidth(), getHeight());
        graphics.blitSprite(
            RenderPipelines.GUI_TEXTURED,
            this.sprite,
            getX() + this.padding, getY() + this.padding,
            getWidth() - this.padding * 2, getHeight() - this.padding * 2
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

    public static class Builder {

        private final int width;
        private final int height;
        private int padding;
        private Identifier sprite;
        private Runnable onPress = () -> {};
        private Component tooltip = null;
        private boolean disabled = false;

        public Builder(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public Builder padding(int padding) {
            this.padding = padding;
            return this;
        }

        public Builder sprite(Identifier sprite) {
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

        public Builder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public SpriteButton build() {
            var button = new SpriteButton(width, height, padding, sprite, onPress, tooltip);
            button.active = !this.disabled;
            return button;
        }
    }
}
