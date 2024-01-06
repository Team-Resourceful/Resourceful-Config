package com.teamresourceful.resourcefulconfig.client.screens.base;

import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.SpriteButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.EqualSpacingLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ModalOverlay extends OverlayScreen {

    protected static final int PADDING = 10;

    protected int modalWidth = 0;
    protected int modalHeight = 0;
    protected int modalLeft = 0;
    protected int modalTop = 0;

    protected int contentWidth = 0;
    protected int contentHeight = 0;
    protected int left = 0;
    protected int top = 0;
    protected Component title = CommonComponents.EMPTY;

    protected ModalOverlay() {
        super(Minecraft.getInstance().screen);
    }

    @Override
    protected void init() {
        this.modalWidth = (int) (this.width * 0.60);
        this.modalHeight = (int) (this.height * 0.80);
        this.modalLeft = (this.width - this.modalWidth) / 2;
        this.modalTop = (this.height - this.modalHeight) / 2;

        EqualSpacingLayout header = new EqualSpacingLayout(this.modalWidth - PADDING * 2, 20, EqualSpacingLayout.Orientation.HORIZONTAL);

        header.addChild(new StringWidget(title, this.font), LayoutSettings::alignVerticallyMiddle);
        header.addChild(
                SpriteButton.builder(12, 12)
                        .sprite(ModSprites.CROSS)
                        .padding(2)
                        .tooltip(CommonComponents.GUI_CANCEL)
                        .onPress(this::onClose)
                        .build(),
                settings -> settings.alignVerticallyMiddle().alignHorizontallyRight()
        );

        this.contentWidth = this.modalWidth;
        this.contentHeight = this.modalHeight;
        this.left = this.modalLeft;
        this.top = this.modalTop;

        header.arrangeElements();
        header.setPosition(this.left + PADDING, this.top + PADDING);
        header.visitWidgets(this::addRenderableWidget);

        this.contentHeight -= header.getHeight() + PADDING * 4;
        this.contentWidth -= PADDING * 2;
        this.top += header.getHeight() + PADDING * 3;
        this.left += PADDING;
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(graphics, mouseX, mouseY, partialTicks);
        renderTransparentBackground(graphics);

        graphics.blitSprite(ModSprites.CONTAINER, this.modalLeft, this.modalTop, this.modalWidth, this.modalHeight);
        graphics.blitSprite(ModSprites.HEADER, this.modalLeft, this.modalTop, this.modalWidth, 20 + PADDING * 2);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int i) {
        if (mouseX < this.modalLeft || mouseX > this.modalLeft + this.modalWidth || mouseY < this.modalTop || mouseY > this.modalTop + this.modalHeight) {
            this.onClose();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, i);
    }

    public void open() {
        Minecraft.getInstance().setScreen(this);
    }
}
