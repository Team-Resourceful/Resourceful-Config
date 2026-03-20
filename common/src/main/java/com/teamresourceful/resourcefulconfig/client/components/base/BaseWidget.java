package com.teamresourceful.resourcefulconfig.client.components.base;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;

public abstract class BaseWidget extends AbstractWidget {

    protected final Font font;
    protected final Minecraft minecraft;

    public BaseWidget(int width, int height) {
        super(0, 0, width, height, CommonComponents.EMPTY);
        this.minecraft = Minecraft.getInstance();
        this.font = this.minecraft.font;
    }

    public void applyCursor(@NotNull GuiGraphicsExtractor graphics) {
        if (!this.isHovered()) return;
        graphics.requestCursor(this.isActive() ? CursorTypes.POINTING_HAND : CursorTypes.NOT_ALLOWED);
    }

    @Override
    protected abstract void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks);

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {

    }
}
