package com.teamresourceful.resourcefulconfig.client.components.options.misc.draggable;

import com.teamresourceful.resourcefulconfig.api.types.info.TooltipProvider;
import com.teamresourceful.resourcefulconfig.api.types.info.Translatable;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.intellij.lang.annotations.MagicConstant;

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

    public void render(GuiGraphics graphics, int x, int y, int mouseX, int mouseY, @MagicConstant(flagsFromClass = DraggableFlags.class) int flags) {
        var hovered = (flags & DraggableFlags.HOVERED) != 0;
        var dragging = (flags & DraggableFlags.DRAGGING) != 0;
        var canDelete = (flags & DraggableFlags.CAN_DELETE) != 0;
        var fadeOut = (flags & DraggableFlags.FADE_OUT) != 0;

        graphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                ModSprites.ofButton(hovered && !dragging),
                x, y, getWidth(), getHeight(), fadeOut ? 0x80FFFFFF : -1
        );
        if (!dragging && hovered) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.DRAGGABLE, x + 4, y + 4, 8, 8, fadeOut ? 0x80FFFFFF : -1);
        }
        if (!dragging && hovered) {
            boolean hoveringDelete = x + getWidth() - 16 <= mouseX;
            if (canDelete) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.DELETE, x + getWidth() - 12, y + 4, 8, 8, fadeOut ? 0x80FFFFFF : -1);
                if (this.minecraft.screen != null && hoveringDelete) {
                    graphics.setTooltipForNextFrame(Component.literal("Remove"), mouseX, mouseY);
                }
            }
            if (!hoveringDelete && this.minecraft.screen != null && value instanceof TooltipProvider provider) {
                if (provider.getTooltip() != null && !provider.getTooltip().getString().isBlank()) {
                    graphics.setTooltipForNextFrame(provider.getTooltip(), mouseX, mouseY);
                }
            }
        }
        int color = hovered ? UIConstants.TEXT_TITLE : UIConstants.TEXT_PARAGRAPH;

        renderScrollingString(
                graphics, this.font, Translatable.toComponent(this.value),
                x + 16, y + 1,
                x + getWidth() - 32, y + getHeight() - 1,
                fadeOut ? ARGB.color(0x80, color) : color
        );
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.list.isDraggingItem() && this.list.getDraggingItem() == this) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.ofButton(true), getX() + 1, getY(), getWidth() - 1, getHeight());
        } else {
            int flags = 0;
            if (this.isHovered()) flags |= DraggableFlags.HOVERED;
            if (this.list.isDraggingItem()) flags |= DraggableFlags.DRAGGING;
            if (this.list.canDelete()) flags |= DraggableFlags.CAN_DELETE;

            render(graphics, getX(), getY(), mouseX, mouseY, flags);
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
