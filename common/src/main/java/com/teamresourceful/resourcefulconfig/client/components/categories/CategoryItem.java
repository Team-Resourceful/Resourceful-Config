package com.teamresourceful.resourcefulconfig.client.components.categories;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class CategoryItem extends BaseWidget implements ListWidget.Item {

    private static final int PADDING = 4;

    private final ConfigScreen screen;
    private final ResourcefulConfig config;
    private final Function<String, List<String>> termCollector;

    public CategoryItem(ConfigScreen screen, ResourcefulConfig config, Function<String, List<String>> termCollector) {
        super(0, PADDING * 2 + Minecraft.getInstance().font.lineHeight);
        this.screen = screen;
        this.config = config;
        this.termCollector = termCollector;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.isHovered()) {
            graphics.blitSprite(RenderType::guiTextured, ModSprites.BUTTON_HOVER, getX() + 1, getY(), getWidth() - 2, getHeight());
        }
        int color = this.isHovered() ? UIConstants.TEXT_TITLE : UIConstants.TEXT_PARAGRAPH;
        renderScrollingString(
                graphics, Minecraft.getInstance().font,
                this.config.info().title().toComponent(),
                getX() + PADDING * 2, getY() + PADDING,
                getX() + PADDING * 2 + getWidth() - PADDING * 4, getY() + getHeight() - PADDING,
                color
        );
    }

    @Override
    public void onClick(double d, double e) {
        Minecraft.getInstance().setScreen(new ConfigScreen(this.screen, this.config, this.termCollector));
    }

    @Override
    public void setItemWidth(int width) {
        this.setWidth(width);
    }
}
