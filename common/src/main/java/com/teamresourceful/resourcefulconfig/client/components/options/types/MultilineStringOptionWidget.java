package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.screens.base.ModalOverlay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MultilineStringOptionWidget extends BaseWidget {

    private static final int WIDTH = 80;
    private static final int SIZE = 12;
    private static final int SPACING = 4;
    private static final int PADDING = 2;

    private final Supplier<String> getter;
    private final Consumer<String> setter;

    public MultilineStringOptionWidget(Supplier<String> getter, Consumer<String> setter) {
        super(WIDTH, 16);
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blitSprite(RenderType::guiTextured, ModSprites.ofButton(this.isHovered()), getX(), getY(), getWidth(), getHeight());

        int contentWidth = font.width(UIConstants.EDIT) + SPACING + SIZE;

        graphics.blitSprite(
                RenderType::guiTextured,
                ModSprites.EDIT,
                getX() + (getWidth() - contentWidth) / 2, getY() + PADDING,
                SIZE, SIZE
        );
        graphics.drawString(
                font, UIConstants.EDIT,
                getX() + (getWidth() - contentWidth) / 2 + SIZE + SPACING,
                getY() + (getHeight() - font.lineHeight) / 2 + 1,
                UIConstants.TEXT_TITLE
        );
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        new MutlilineStringOverlay(getter, setter).open();
    }


    private static class MutlilineStringOverlay extends ModalOverlay {

        private final Supplier<String> getter;
        private final Consumer<String> setter;

        protected MutlilineStringOverlay(Supplier<String> getter, Consumer<String> setter) {
            super();
            this.title = UIConstants.EDIT_STRING;

            this.getter = getter;
            this.setter = setter;
        }

        @Override
        protected void init() {
            super.init();

            MultiLineEditBox box = addRenderableWidget(new MultiLineEditBox(
                    font,
                    left, top,
                    contentWidth, contentHeight,
                    CommonComponents.EMPTY, CommonComponents.EMPTY
            ) {
                @Override
                protected void renderBackground(GuiGraphics guiGraphics) {

                }
            });

            box.setValue(getter.get());
            box.setValueListener(setter);
        }

        @Override
        public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.renderBackground(graphics, mouseX, mouseY, partialTicks);

            graphics.blitSprite(RenderType::guiTextured, ModSprites.BUTTON, left, top, contentWidth, contentHeight);
        }
    }
}
