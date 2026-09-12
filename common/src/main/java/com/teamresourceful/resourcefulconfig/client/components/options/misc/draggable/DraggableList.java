package com.teamresourceful.resourcefulconfig.client.components.options.misc.draggable;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefulconfig.client.components.base.ListWidget;
import com.teamresourceful.resourcefulconfig.client.utils.KeyCodeHelper;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2d;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class DraggableList<T> extends ListWidget {

    private final Vector2d draggingOffset = new Vector2d();
    private int draggingIndex = -1;
    private Consumer<List<T>> onUpdate = _ -> {};
    private boolean canDelete = true;

    public DraggableList(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setOnUpdate(Consumer<List<T>> onUpdate) {
        this.onUpdate = onUpdate;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean canDelete() {
        return this.canDelete;
    }

    public void addAll(List<T> values) {
        this.items.clear();
        for (T value : values) {
            AtomicReference<DraggableItem<T>> item = new AtomicReference<>();
            item.set(new DraggableItem<>(this.width, this, value, () -> {
                this.items.remove(item.get());
                //noinspection unchecked
                this.onUpdate.accept(this.items.stream()
                        .map(i -> (DraggableItem<T>) i)
                        .map(DraggableItem::value)
                        .toList());

                this.updateScrollBar();
            }));
            this.items.add(item.get());
        }
        this.updateLastHeight();
    }

    @Deprecated
    @Override
    public void add(Item item) {
        throw new UnsupportedOperationException("Use addAll(T value) instead.");
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractWidgetRenderState(graphics, mouseX, mouseY, partialTicks);

        if (!this.isMouseOver(mouseX, mouseY) && this.draggingIndex != -1 && !KeyCodeHelper.isMouseKeyPressed(InputConstants.MOUSE_BUTTON_LEFT)) {
            this.draggingIndex = -1;
        }

        graphics.enableScissor(getX(), getY(), getX() + width, getY() + height);
        extractPossiblePositionLine(graphics, mouseX, mouseY);
        graphics.disableScissor();

        extractDraggedItem(graphics, mouseX, mouseY);
    }

    private void extractPossiblePositionLine(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.draggingIndex == -1) return;
        int hoveredIndex = this.getItemOver(mouseX, mouseY);
        if (hoveredIndex != this.draggingIndex && hoveredIndex != -1) {
            Item hoveredItem = this.items.get(hoveredIndex);
            boolean isAbove = hoveredIndex < this.draggingIndex;
            int y = hoveredItem.getY() + (isAbove ? -1 : hoveredItem.getHeight() - 1);
            graphics.fill(
                    hoveredItem.getX(), y,
                    hoveredItem.getX() + hoveredItem.getWidth(), y + 2,
                    0xff55ff55
            );
        }
    }

    private void extractDraggedItem(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (this.draggingIndex == -1) return;
        Item item = this.items.get(this.draggingIndex);
        if (!(item instanceof DraggableItem<?> draggableItem)) return;

        graphics.nextStratum();

        int x = (int) (mouseX - this.draggingOffset.x);
        int y = (int) (mouseY - this.draggingOffset.y);
        draggableItem.extract(graphics, x, y, mouseX, mouseY, DraggableFlags.HOVERED | DraggableFlags.FADE_OUT);
    }

    public int getItemOver(double mouseX, double mouseY) {
        for (int i = 0; i < this.items.size(); i++) {
            if (this.items.get(i).isMouseOver(mouseX, mouseY)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean bl) {
        if (super.mouseClicked(event, bl)) return true;
        if (event.input() == 0) {
            this.draggingIndex = this.getItemOver(event.x(), event.y());
            if (this.draggingIndex != -1) {
                Item item = this.items.get(this.draggingIndex);
                this.draggingOffset.set(event.x() - item.getX(), event.y() - item.getY());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(@NotNull MouseButtonEvent event) {
        if (event.input() == 0 && this.draggingIndex != -1) {
            int newIndex = this.getItemOver(event.x(), event.y());
            if (newIndex != -1 && newIndex != this.draggingIndex) {
                this.items.add(newIndex, this.items.remove(this.draggingIndex));
                //noinspection unchecked
                this.onUpdate.accept(this.items.stream()
                        .map(item -> (DraggableItem<T>) item)
                        .map(DraggableItem::value)
                        .toList());
            }
            this.draggingIndex = -1;
        }
        return super.mouseReleased(event);
    }

    public boolean isDraggingItem() {
        return this.draggingIndex != -1;
    }

    public Item getDraggingItem() {
        return this.draggingIndex != -1 ? this.items.get(this.draggingIndex) : null;
    }
}
