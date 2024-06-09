package com.teamresourceful.resourcefulconfig.client.components.header;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigCategory;
import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.ContainerWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton;
import com.teamresourceful.resourcefulconfig.client.components.options.types.StringOptionWidget;
import com.teamresourceful.resourcefulconfig.client.utils.ConfigSearching;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class HeaderControlsWidget extends ContainerWidget {

    private final LinearLayout layout;

    public HeaderControlsWidget(int width, ResourcefulConfig config, Runnable onSearchUpdate) {
        super(0, 0, width, 0);

        this.layout = LinearLayout.horizontal().spacing(5);

        boolean willGoBack = config instanceof ResourcefulConfigCategory;

        this.layout.addChild(SpriteButton.builder(12, 12)
                .padding(2)
                .sprite(!willGoBack ? ModSprites.CROSS : ModSprites.CHEVRON_LEFT)
                .onPress(() -> Minecraft.getInstance().screen.onClose())
                .tooltip(!willGoBack ? UIConstants.CLOSE : UIConstants.BACK)
                .build());

        var searchWidget = new StringOptionWidget(ConfigSearching::getSearch, name -> {
            if (ConfigSearching.setSearch(name)) {
                onSearchUpdate.run();
            }
            return true;
        }, false);
        searchWidget.setWidth(this.width - UIConstants.PAGE_PADDING * 2 - 16 - 5);
        searchWidget.setHint(Component.literal("Search...").withColor(UIConstants.TEXT_PARAGRAPH));
        this.layout.addChild(searchWidget);

        this.layout.arrangeElements();
        this.layout.visitWidgets(this::addRenderableWidget);

        this.height = this.layout.getHeight() + UIConstants.PAGE_PADDING * 2;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(ModSprites.CONTAINER, getX(), getY(), width, height);
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void positionUpdated() {
        this.layout.setPosition(this.getX() + UIConstants.PAGE_PADDING, this.getY() + UIConstants.PAGE_PADDING);
    }
}
