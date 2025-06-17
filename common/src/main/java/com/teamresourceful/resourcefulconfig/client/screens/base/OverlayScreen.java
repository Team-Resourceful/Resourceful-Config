package com.teamresourceful.resourcefulconfig.client.screens.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
        this.background.resize(Minecraft.getInstance(), this.width, this.height);
        if (this.background instanceof OverlayScreen overlay) overlay.isInitialized = false;
        if (this.isInitialized) {
            Minecraft.getInstance().setScreen(this.background);
        } else {
            rebuildWidgets();
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics graphics, int i, int j, float f) {
        this.background.renderWithTooltip(graphics, -1, -1, f);
        graphics.nextStratum();
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.background);
    }
}
