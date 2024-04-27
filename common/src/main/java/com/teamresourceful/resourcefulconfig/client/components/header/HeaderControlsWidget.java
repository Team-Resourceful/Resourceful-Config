package com.teamresourceful.resourcefulconfig.client.components.header;

import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.ContainerWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class HeaderControlsWidget extends ContainerWidget {

    private final LinearLayout layout;

    public HeaderControlsWidget(@Nullable String filename, int width) {
        super(0, 0, width, 0);

        this.layout = LinearLayout.vertical().spacing(5);

        Screen screen = Minecraft.getInstance().screen;
        boolean willClose = screen instanceof ConfigScreen config && config.getParent() == null;

        this.layout.addChild(SpriteButton.builder(12, 12)
                .padding(2)
                .sprite(willClose ? ModSprites.CROSS : ModSprites.CHEVRON_LEFT)
                .onPress(() -> Minecraft.getInstance().screen.onClose())
                .tooltip(willClose ? UIConstants.CLOSE : UIConstants.BACK)
                .build());

        this.layout.addChild(
                new StringWidget(
                        this.width, 10,
                        Component.literal(Optionull.mapOrDefault(filename, name -> name + ".jsonc", ""))
                                .withColor(UIConstants.TEXT_PARAGRAPH),
                        Minecraft.getInstance().font
                ).alignLeft(),
                LayoutSettings::alignVerticallyMiddle
        );

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
