package com.teamresourceful.resourcefulconfig.client.components.categories;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.client.ConfigScreenContext;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import org.jetbrains.annotations.NotNull;

public class CategoryItem extends BaseWidget implements ListWidget.Item {

    private static final int PADDING = 4;

    private final ConfigScreen screen;
    private final ResourcefulConfig config;
    private final ConfigScreenContext context;

    public CategoryItem(ConfigScreen screen, ResourcefulConfig config, ConfigScreenContext context) {
        super(0, PADDING * 2 + Minecraft.getInstance().font.lineHeight);
        this.screen = screen;
        this.config = config;
        this.context = context;
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isHovered()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.BUTTON_HOVER, getX() + 1, getY(), getWidth() - 2, getHeight());
        }
        int color = this.isHovered() ? UIConstants.TEXT_TITLE : UIConstants.TEXT_PARAGRAPH;

        graphics.textRendererForWidget(
                this,
                GuiGraphicsExtractor.HoveredTextEffects.NONE
        ).acceptScrollingWithDefaultCenter(
                this.config.info().title().toComponent().copy().withColor(color),
                getX() + PADDING * 2, getX() + PADDING * 2 + getWidth() - PADDING * 4,
                getY() + PADDING, getY() + getHeight() - PADDING
        );
    }

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean bl) {
        Minecraft.getInstance().setScreen(new ConfigScreen(this.screen, this.config, this.context));
    }

    @Override
    public void setItemWidth(int width) {
        this.setWidth(width);
    }
}
