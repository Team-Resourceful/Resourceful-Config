package com.teamresourceful.resourcefulconfig.client.components.configs;

import com.teamresourceful.resourcefulconfig.api.client.ResourcefulConfigScreen;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.ContainerWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

public class ConfigItem extends ContainerWidget implements ListWidget.Item {

    private final Component title;
    private final Component description;
    private final ResourcefulConfig config;

    public ConfigItem(ResourcefulConfig config) {
        super(0, 0, 0, 0);
        this.title = config.info().title().toComponent().withColor(UIConstants.TEXT_TITLE);
        this.description = config.info().description().toComponent().withColor(UIConstants.TEXT_PARAGRAPH);
        this.config = config;
    }

    public void init() {
        clear();

        Font font = Minecraft.getInstance().font;

        int width = this.width - UIConstants.PAGE_PADDING * 4;

        LinearLayout titleDesc = LinearLayout
                .vertical()
                .spacing(UIConstants.SPACING);

        titleDesc.addChild(new StringWidget(width, 9, this.title, font).alignLeft());
        titleDesc.addChild(new StringWidget(width, 9, this.description, font).alignLeft());

        titleDesc.arrangeElements();
        titleDesc.setPosition(this.getX() + UIConstants.PAGE_PADDING * 2, this.getY() + UIConstants.PAGE_PADDING * 2);
        titleDesc.visitWidgets(this::addRenderableWidget);
        this.height = titleDesc.getHeight() + UIConstants.PAGE_PADDING * 4;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(
                RenderType::guiTextured,
                ModSprites.ofButton(this.isHovered()),
                getX() + UIConstants.PAGE_PADDING, getY() + UIConstants.PAGE_PADDING,
                width - UIConstants.PAGE_PADDING * 2, height - UIConstants.PAGE_PADDING * 2
        );
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void positionUpdated() {
        init();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isHoveredOrFocused()) {
            Minecraft.getInstance().setScreen(
                    ResourcefulConfigScreen.get(Minecraft.getInstance().screen, this.config)
            );
        }
        return false;
    }

    @Override
    public void setItemWidth(int width) {
        this.setWidth(width);
    }
}
