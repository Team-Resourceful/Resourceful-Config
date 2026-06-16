package com.teamresourceful.resourcefulconfig.client.components.options.misc.draggable;

import com.teamresourceful.resourcefulconfig.api.types.info.TooltipProvider;
import com.teamresourceful.resourcefulconfig.api.types.info.Translatable;
import com.teamresourceful.resourcefulconfig.client.UIConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

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

    public void extract(GuiGraphicsExtractor graphics, int x, int y, int mouseX, int mouseY, @MagicConstant(flagsFromClass = DraggableFlags.class) int flags) {
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
                if (this.minecraft.gui.screen() != null && hoveringDelete) {
                    graphics.setTooltipForNextFrame(Component.literal("Remove"), mouseX, mouseY);
                }
            }
            if (!hoveringDelete && this.minecraft.gui.screen() != null && value instanceof TooltipProvider provider) {
                if (provider.getTooltip() != null && !provider.getTooltip().getString().isBlank()) {
                    Font font = this.minecraft.font;
                    graphics.setTooltipForNextFrame(font, font.split(provider.getTooltip(), Integer.MAX_VALUE), mouseX, mouseY);
                }
            }

            this.applyCursor(graphics);
        }
        int color = hovered ? UIConstants.TEXT_TITLE : UIConstants.TEXT_PARAGRAPH;


        int left = x + 16;
        int right = x + getWidth() - 32;
        var renderer = graphics.textRendererForWidget(
                this,
                GuiGraphicsExtractor.HoveredTextEffects.NONE
        );
        renderer.acceptScrolling(
                Translatable.toComponent(this.value).copy().withColor(fadeOut ? ARGB.color(0x80, color) : color),
                (left + right) / 2,
                left,
                right,
                y + 1,
                y + getHeight() - 1,
                renderer.defaultParameters().withOpacity(fadeOut ? 0x80 : color)
        );
    }

    @Override
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.list.isDraggingItem() && this.list.getDraggingItem() == this) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.ofButton(true), getX() + 1, getY(), getWidth() - 1, getHeight());
        } else {
            int flags = 0;
            if (this.isHovered()) flags |= DraggableFlags.HOVERED;
            if (this.list.isDraggingItem()) flags |= DraggableFlags.DRAGGING;
            if (this.list.canDelete()) flags |= DraggableFlags.CAN_DELETE;

            extract(graphics, getX(), getY(), mouseX, mouseY, flags);
        }
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean bl) {
        if (this.isHovered() && event.button() == 0 && event.x() >= getX() + getWidth() - 16) {
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
