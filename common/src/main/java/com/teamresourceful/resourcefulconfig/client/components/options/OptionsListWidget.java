package com.teamresourceful.resourcefulconfig.client.components.options;

import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import org.jetbrains.annotations.NotNull;

public class OptionsListWidget extends ListWidget {

    public OptionsListWidget(int width, int height) {
        super(0, 0, width, height);
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.CONTAINER, getX(), getY(), getWidth(), getHeight());
        super.extractWidgetRenderState(graphics, mouseX, mouseY, partialTicks);
    }
}
