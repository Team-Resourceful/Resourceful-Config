package com.teamresourceful.resourcefulconfig.client.components.options.misc.draggable;

import com.teamresourceful.resourcefulconfig.api.types.info.Translatable;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class DraggableItem<T> extends BaseWidget implements ListWidget.Item {

    private static final int HEIGHT = 16;

    private final DraggableList<T> list;
    private final T value;
    private final Runnable remove;

    public DraggableItem(int width, DraggableList<T> list, T value, Runnable remove) {
        super(width, HEIGHT);
        this.list = list;
        this.value = value;
        this.remove = remove;
    }

    public void render(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, boolean hovered, boolean dragging, boolean canDelete) {
        graphics.blitSprite(
                ModSprites.ofButton(hovered && !dragging),
                x, y, getWidth(), getHeight()
        );
        if (!dragging && hovered) {
            graphics.blitSprite(ModSprites.DRAGGABLE, x + 4, y + 4, 8, 8);
        }
        if (!dragging && hovered && canDelete) {
            graphics.blitSprite(ModSprites.DELETE, x + getWidth() - 12, y + 4, 8, 8);
            if (this.minecraft.screen != null && x + getWidth() - 16 <= mouseX) {
                this.minecraft.screen.setTooltipForNextRenderPass(Component.literal("Remove"));
            }
        }
        int color = hovered ? UIConstants.TEXT_TITLE : UIConstants.TEXT_PARAGRAPH;

        renderScrollingString(
                graphics, this.font, Translatable.toComponent(this.value),
                x + 16, y + 1,
                x + getWidth() - 32, y + getHeight() - 1,
                color
        );
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.list.isDraggingItem() && this.list.getDraggingItem() == this) {
            graphics.blitSprite(ModSprites.ofButton(true), getX() + 1, getY(), getWidth() - 1, getHeight());
        } else {
            render(graphics, getX(), getY(), mouseX, mouseY, this.isHovered(), this.list.isDraggingItem(), this.list.canDelete());
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isHovered() && button == 0 && mouseX >= getX() + getWidth() - 16) {
            this.remove.run();
            return true;
        }
        return false;
    }


    @Override
    public void setItemWidth(int width) {
        this.setWidth(width);
    }

    public T value() {
        return this.value;
    }
}
