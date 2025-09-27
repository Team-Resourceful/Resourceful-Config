package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.screens.base.ModalOverlay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.MultiLineEditBox;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;

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
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.ofButton(this.isHovered()), getX(), getY(), getWidth(), getHeight());

        int contentWidth = font.width(UIConstants.EDIT) + SPACING + SIZE;

        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
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

        this.applyCursor(graphics);
    }

    @Override
    public void onClick(@NotNull MouseButtonEvent event, boolean bl) {
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

            var box = addRenderableWidget(MultiLineEditBox.builder()
                    .setShowBackground(false)
                    .setX(left)
                    .setY(top)
                    .setTextShadow(false)
                    .build(font, contentWidth, contentHeight, CommonComponents.EMPTY)
            );

            box.setValue(getter.get());
            box.setValueListener(setter);
        }

        @Override
        public void renderBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.renderBackground(graphics, mouseX, mouseY, partialTicks);

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.BUTTON, left, top, contentWidth, contentHeight);
        }
    }
}
