package com.teamresourceful.resourcefulconfig.client.components.options.types.color;

import com.mojang.blaze3d.platform.NativeImage;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton;
import com.teamresourceful.resourcefulconfig.client.screens.base.OverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class EyedropperButton extends SpriteButton {

    private static final Identifier SCREEN_TEXTURE = Identifier.fromNamespaceAndPath("resourcefulconfig", "dynamic/screen");

    public EyedropperButton(HsbState state) {
        super(12, 12, 2, ModSprites.EYE_DROPPER, () -> {
            Minecraft minecraft = Minecraft.getInstance();
            Screenshot.takeScreenshot(minecraft.getMainRenderTarget(), image -> {
                DynamicTexture texture = new DynamicTexture(() -> "Resourceful Config Eyedropper Screenshot", image);
                minecraft.getTextureManager().register(SCREEN_TEXTURE, texture);
                minecraft.setScreen(new Overlay(minecraft.screen, texture.getPixels(), state));
            });
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
        public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
            double guiScale = Minecraft.getInstance().getWindow().getGuiScale();

            graphics.blit(SCREEN_TEXTURE, 0, 0, this.width, this.height, 0f, 1f, 0f, 1f);

            int x = mouseX - 5;
            int y = mouseY - 5;

            float u0 = (float) x / (float) this.width;
            float v0 = (float) y / (float) this.height;
            float u1 = (float) (x + 10) / (float) this.width;
            float v1 = (float) (y + 10) / (float) this.height;

            graphics.blit(SCREEN_TEXTURE, x - 5, y - 5, x + 15, y + 15, u0, u1, v0, v1);

            graphics.outline(x - 5, y - 5, 20, 20, 0xFFFFFFFF);

            int pixelX = (int) (mouseX * guiScale);
            int pixelY = (int) (mouseY * guiScale);

            if (pixelX < 0 || pixelY < 0 || pixelX >= this.image.getWidth() || pixelY >= this.image.getHeight()) return;

            int pixel = this.image.getPixel(pixelX, pixelY);

            graphics.setTooltipForNextFrame(Component.literal(String.format("#%08X", pixel)).withColor(pixel), mouseX, mouseY);
        }

        @Override
        public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean bl) {
            if (event.input() != 0) return false;
            double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
            int pixelX = (int) (event.x() * guiScale);
            int pixelY = (int) (event.y() * guiScale);
            if (pixelX < 0 || pixelY < 0 || pixelX >= this.image.getWidth() || pixelY >= this.image.getHeight()) return false;
            this.state.set(HsbColor.fromRgb(this.image.getPixel(pixelX, pixelY)));
            this.onClose();
            return true;
        }
    }


}
