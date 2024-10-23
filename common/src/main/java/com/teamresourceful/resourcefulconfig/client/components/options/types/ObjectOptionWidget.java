package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigObjectEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.Translatable;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.Options;
import com.teamresourceful.resourcefulconfig.client.components.options.OptionsListWidget;
import com.teamresourceful.resourcefulconfig.client.screens.base.ModalOverlay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

import java.util.List;

public class ObjectOptionWidget extends BaseWidget {

    private static final int WIDTH = 100;
    private static final int SIZE = 12;
    private static final int SPACING = 4;
    private static final int PADDING = 2;

    private final ResourcefulConfigObjectEntry entry;

    public ObjectOptionWidget(ResourcefulConfigObjectEntry entry) {
        super(WIDTH, 16);

        this.entry = entry;
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
        new ObjectEditOverlay(this.entry).open();
    }


    private static class ObjectEditOverlay extends ModalOverlay {

        private final ResourcefulConfigObjectEntry entry;

        protected ObjectEditOverlay(ResourcefulConfigObjectEntry entry) {
            super();
            this.entry = entry;

            this.title = Translatable.toSpeifiedComponent(entry.instance(), UIConstants.EDIT_OBJECT);
        }

        @Override
        protected void init() {
            super.init();

            OptionsListWidget list = addRenderableWidget(new OptionsListWidget(this.contentWidth, this.contentHeight));
            list.setPosition(this.left, this.top);
            Options.populateOptions(list, this.entry.entries(), List.of());
        }

        @Override
        public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.renderBackground(graphics, mouseX, mouseY, partialTicks);
        }
    }
}
