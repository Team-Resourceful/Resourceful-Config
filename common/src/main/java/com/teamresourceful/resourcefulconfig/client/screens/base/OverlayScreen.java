package com.teamresourceful.resourcefulconfig.client.screens.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;

public abstract class OverlayScreen extends Screen {

    private final Screen background;
    private boolean isInitialized = false;

    protected OverlayScreen(Screen background) {
        super(CommonComponents.EMPTY);
        this.background = background;
    }

    @Override
    public void added() {
        super.added();
        this.background.clearFocus();
    }

    @Override
    protected void init() {
        super.init();
        if (!this.isInitialized) {
            this.isInitialized = true;
        }
    }

    @Override
    protected void repositionElements() {
        this.background.resize(this.width, this.height);
        if (this.background instanceof OverlayScreen overlay) overlay.isInitialized = false;
        if (this.isInitialized) {
            Minecraft.getInstance().gui.setScreen(this.background);
        } else {
            rebuildWidgets();
        }
    }

    @Override
    public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        this.background.extractRenderStateWithTooltipAndSubtitles(graphics, -1, -1, partialTicks);
        graphics.nextStratum();
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().gui.setScreen(this.background);
    }
}
