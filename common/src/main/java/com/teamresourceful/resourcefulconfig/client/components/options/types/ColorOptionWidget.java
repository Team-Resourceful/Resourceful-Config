package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton;
import com.teamresourceful.resourcefulconfig.client.screens.base.OverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.resources.ResourceLocation;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class ColorOptionWidget extends BaseWidget {

    private static final int SIZE = 16;
    private static final int PADDING = 4;
    private static final int SPACING = 2;

    private final int[] presets;
    private final IntSupplier getter;
    private final IntConsumer setter;

    public ColorOptionWidget(int[] presets, IntSupplier getter, IntConsumer setter) {
        super(SIZE, SIZE);
        this.presets = presets;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(ModSprites.BUTTON, getX(), getY(), this.height, this.height);
        graphics.fill(getX() + 1, getY() + 1, getX() + this.height - 1, getY() + this.height - 1, this.getter.getAsInt());
    }

    @Override
    public void onClick(double d, double e) {
        if (this.presets.length == 0) return;
        Minecraft.getInstance().setScreen(new PresetsOverlay(this));
    }

    private static class PresetsOverlay extends OverlayScreen {

        private final ColorOptionWidget widget;

        private int x;
        private int y;
        private int width;
        private int height;

        protected PresetsOverlay(ColorOptionWidget widget) {
            super(Minecraft.getInstance().screen);
            this.widget = widget;
        }

        @Override
        protected void init() {
            GridLayout layout = new GridLayout().spacing(SPACING);

            GridLayout.RowHelper helper = layout.createRowHelper(5);

            for (int color : this.widget.presets) {

                helper.addChild(new ColorButton(12, 12, 1, () -> {
                    this.widget.setter.accept(color);
                    this.onClose();
                }, color & 0x00FFFFFF | 0xFF000000));
            }

            layout.arrangeElements();

            int y = this.widget.getY() > this.height / 2 ?
                    this.widget.getY() - layout.getHeight() - PADDING * 2 - SPACING :
                    this.widget.getY() + this.widget.getHeight() + PADDING * 2 + SPACING;

            layout.setPosition(this.widget.getX() + PADDING, y + PADDING);
            layout.visitWidgets(this::addRenderableWidget);

            this.x = this.widget.getX();
            this.y = y;
            this.width = layout.getWidth() + PADDING * 2;
            this.height = layout.getHeight() + PADDING * 2;
        }

        @Override
        public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.renderBackground(graphics, mouseX, mouseY, partialTicks);
            graphics.blitSprite(ModSprites.ACCENT, this.x, this.y, this.width, this.height);
            graphics.blitSprite(ModSprites.BUTTON, this.x + 1, this.y + 1, this.width - 2, this.height - 2);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            super.mouseClicked(mouseX, mouseY, button);
            this.onClose();
            return true;
        }
    }

    private static class ColorButton extends SpriteButton {

        private final int color;

        protected ColorButton(int width, int height, int padding, Runnable onPress, int color) {
            super(width, height, padding, null, onPress, null);
            this.color = color;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            ResourceLocation button = isHovered() ? ModSprites.BUTTON_HOVER : ModSprites.BUTTON;
            graphics.blitSprite(button, getX(), getY(), getWidth(), getHeight());
            graphics.fill(
                    getX() + this.padding,
                    getY() + this.padding,
                    getX() + getWidth() - this.padding,
                    getY() + getHeight() - this.padding,
                    this.color
            );
        }
    }
}
