package com.teamresourceful.resourcefulconfig.client.components.header;

import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfoButton;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.ContainerWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.EqualSpacingLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;

public class HeaderContentWidget extends ContainerWidget {

    private final EqualSpacingLayout layout;

    public HeaderContentWidget(int width, ResourcefulConfig config) {
        super(0, 0, width, 0);

        Font font = Minecraft.getInstance().font;
        int twoThirds = (int) (this.width * (2f / 3f));

        this.layout = new EqualSpacingLayout(this.width - UIConstants.PAGE_PADDING * 2, 0, EqualSpacingLayout.Orientation.HORIZONTAL);

        LinearLayout titleDesc = LinearLayout
                .vertical()
                .spacing(UIConstants.SPACING);

        titleDesc.addChild(
                new StringWidget(twoThirds, 9, config.info().title().toComponent().withColor(UIConstants.TEXT_TITLE), font)
                        .alignLeft()
        );

        titleDesc.addChild(
                new StringWidget(twoThirds, 9, config.info().description().toComponent().withColor(UIConstants.TEXT_PARAGRAPH), font)
                        .alignLeft()
        );

        LinearLayout links = LinearLayout
                .horizontal()
                .spacing(UIConstants.SPACING);

        for (ResourcefulConfigLink link : config.info().links()) {
            SpriteButton button = SpriteButton.builder(12, 12)
                    .padding(2)
                    .sprite(ModSprites.ofIcon(link.icon()))
                    .onPress(() -> {
                        Screen screen = Minecraft.getInstance().screen;
                        if (screen == null) return;
                        ConfirmLinkScreen.confirmLinkNow(screen, link.url());
                    })
                    .tooltip(link.text().toComponent())
                    .build();
            links.addChild(button);
        }

        for (ResourcefulConfigInfoButton infoButton : config.info().buttons()) {
            SpriteButton button = SpriteButton.builder(12, 12)
                    .padding(2)
                    .sprite(ModSprites.ofIcon(infoButton.icon()))
                    .onPress(infoButton::onClick)
                    .tooltip(infoButton.text().toComponent())
                    .build();
            links.addChild(button);
        }

        this.layout.addChild(titleDesc);
        this.layout.addChild(links, settings -> settings.alignVerticallyMiddle().alignHorizontallyRight());
        this.layout.arrangeElements();
        this.layout.visitWidgets(this::addRenderableWidget);

        this.height = this.layout.getHeight() + UIConstants.PAGE_PADDING * 2;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderType::guiTextured, ModSprites.CONTAINER, getX(), getY(), width, height);
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void positionUpdated() {
        this.layout.setPosition(this.getX() + UIConstants.PAGE_PADDING, this.getY() + UIConstants.PAGE_PADDING);
    }
}
