package com.teamresourceful.resourcefulconfig.client.components.options.types.color;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.mixins.client.GuiGraphicsExtractorAccessor;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public class SaturationBrightnessSelector extends BaseWidget {

    private final HsbState state;

    public SaturationBrightnessSelector(int width, int height, HsbState state) {
        super(width, height);
        this.state = state;
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        HsbColor color = state.get();

        int posX = Math.round(color.saturation() * this.getWidth());
        int posY = this.getHeight() - Math.round(color.brightness() * this.getHeight());

        int tileWidth = Math.round(this.getWidth() / 10f);
        int tileHeight = Math.round(this.getHeight() / 10f);

        for (int dy = 0; dy < 10; dy++) {
            float minB = dy / 10f;
            float maxB = (dy + 1) / 10f;
            for (int dx = 0; dx < 10; dx++) {
                float minS = dx / 10f;
                float maxS = (dx + 1) / 10f;
                extractGradient(
                        graphics,
                        getX() + dx * tileWidth, getY() + (10 - dy - 1) * tileHeight,
                        tileWidth, tileHeight,
                        HsbColor.of(color.hue(), minS, maxB, 255).toRgba(),
                        HsbColor.of(color.hue(), maxS, maxB, 255).toRgba(),
                        HsbColor.of(color.hue(), minS, minB, 255).toRgba(),
                        HsbColor.of(color.hue(), maxS, minB, 255).toRgba()
                );
            }
        }

        graphics.outline(getX() + posX - 1, getY() + posY - 1, 3, 3, 0xFF000000);
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean bl) {
        if (event.input() != 0) return false;
        if (!isMouseOver(event.x(), event.y())) return false;
        int x = (int) event.x() - this.getX();
        int y = (int) event.y() - this.getY();
        if (x < 0 || x >= this.getWidth() || y < 0 || y >= this.getHeight()) return false;
        this.state.set(HsbColor.of(
                this.state.get().hue(),
                Mth.clamp(x / (float) this.getWidth(), 0f, 1f),
                1 - Mth.clamp(y / (float) this.getHeight(), 0f, 1f),
                255
        ));
        return true;
    }

    @Override
    public boolean mouseDragged(@NotNull MouseButtonEvent event, double d, double e) {
        return this.mouseClicked(event, false);
    }

    private void extractGradient(
            GuiGraphicsExtractor graphics,
            int x, int y,
            int width, int height,
            int topLeft, int topRight, int bottomLeft, int bottomRight
    ) {
        var access = (GuiGraphicsExtractorAccessor) graphics;

        access.getGuiRenderState().addGuiElement(new GradientRenderState(
                new Matrix3x2f(graphics.pose()),
                x, y, x + width, y + height,
                topLeft, topRight, bottomLeft, bottomRight
        ));
    }

    private record GradientRenderState(
            Matrix3x2f pose,
            int x0,
            int y0,
            int x1,
            int y1,
            int col1,
            int col2,
            int col3,
            int col4
    ) implements GuiElementRenderState {

        @Override
        public void buildVertices(@NotNull VertexConsumer consumer) {
            consumer.addVertexWith2DPose(this.pose(), (float)this.x0(), (float)this.y0()).setColor(this.col1());
            consumer.addVertexWith2DPose(this.pose(), (float)this.x0(), (float)this.y1()).setColor(this.col3());
            consumer.addVertexWith2DPose(this.pose(), (float)this.x1(), (float)this.y1()).setColor(this.col4());
            consumer.addVertexWith2DPose(this.pose(), (float)this.x1(), (float)this.y0()).setColor(this.col2());
        }

        @Override
        public @NotNull RenderPipeline pipeline() {
            return RenderPipelines.GUI;
        }

        @Override
        public @NotNull TextureSetup textureSetup() {
            return TextureSetup.noTexture();
        }

        @Override
        public @Nullable ScreenRectangle bounds() {
            // This is ugly
            return Optionull.map(Minecraft.getInstance().screen, Screen::getRectangle);
        }

        @Override
        public @Nullable ScreenRectangle scissorArea() {
            return Optionull.map(Minecraft.getInstance().screen, Screen::getRectangle);
        }
    }
}
