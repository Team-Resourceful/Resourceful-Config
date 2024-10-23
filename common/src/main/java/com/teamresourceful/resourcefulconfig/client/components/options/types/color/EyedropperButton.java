package com.teamresourceful.resourcefulconfig.client.components.options.types.color;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton;
import com.teamresourceful.resourcefulconfig.client.screens.base.OverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class EyedropperButton extends SpriteButton {

    private static final ResourceLocation SCREEN_TEXTURE = ResourceLocation.fromNamespaceAndPath("resourcefulconfig", "dynamic/screen");

    public EyedropperButton(HsbState state) {
        super(12, 12, 2, ModSprites.EYE_DROPPER, () -> {
            Minecraft minecraft = Minecraft.getInstance();
            DynamicTexture texture = new DynamicTexture(Screenshot.takeScreenshot(minecraft.getMainRenderTarget()));
            minecraft.getTextureManager().register(SCREEN_TEXTURE, texture);
            minecraft.setScreen(new Overlay(minecraft.screen, texture.getPixels(), state));
        }, null);
    }

    private static class Overlay extends OverlayScreen {

        private final NativeImage image;
        private final HsbState state;

        protected Overlay(Screen background, NativeImage image, HsbState state) {
            super(background);
            this.image = image;
            this.state = state;
        }

        @Override
        public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            double guiScale = Minecraft.getInstance().getWindow().getGuiScale();

            RenderSystem.setShaderTexture(0, SCREEN_TEXTURE);
            RenderSystem.setShader(CoreShaders.POSITION_TEX);
            Matrix4f matrix4f = graphics.pose().last().pose();
            BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            bufferBuilder.addVertex(matrix4f, 0f, this.height, 0f).setUv(0f, 1f);
            bufferBuilder.addVertex(matrix4f, this.width, this.height, 0f).setUv(1f, 1f);
            bufferBuilder.addVertex(matrix4f, this.width, 0f, 0f).setUv(1f, 0f);
            bufferBuilder.addVertex(matrix4f, 0f, 0f, 0f).setUv(0f, 0f);
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());


            int x = mouseX - 5;
            int y = mouseY - 5;

            matrix4f = graphics.pose().last().pose();
            bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            float u0 = (float) x / (float) this.width;
            float v0 = (float) y / (float) this.height;
            float u1 = (float) (x + 10) / (float) this.width;
            float v1 = (float) (y + 10) / (float) this.height;

            bufferBuilder.addVertex(matrix4f, x - 5, y + 15, 0f).setUv(u0, v1);
            bufferBuilder.addVertex(matrix4f, x + 15, y + 15, 0f).setUv(u1, v1);
            bufferBuilder.addVertex(matrix4f, x + 15, y - 5, 0f).setUv(u1, v0);
            bufferBuilder.addVertex(matrix4f, x - 5, y - 5, 0f).setUv(u0, v0);
            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());

            graphics.renderOutline(x - 5, y - 5, 20, 20, 0xFFFFFFFF);

            int pixelX = (int) (mouseX * guiScale);
            int pixelY = (int) (mouseY * guiScale);

            if (pixelX < 0 || pixelY < 0 || pixelX >= this.image.getWidth() || pixelY >= this.image.getHeight()) return;

            int pixel = this.image.getPixel(pixelX, pixelY);

            setTooltipForNextRenderPass(Component.literal(String.format("#%08X", pixel)).withColor(pixel));
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button != 0) return false;
            double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
            int pixelX = (int) (mouseX * guiScale);
            int pixelY = (int) (mouseY * guiScale);
            if (pixelX < 0 || pixelY < 0 || pixelX >= this.image.getWidth() || pixelY >= this.image.getHeight()) return false;
            this.state.set(HsbColor.fromRgb(this.image.getPixel(pixelX, pixelY)));
            this.onClose();
            return true;
        }
    }


}
