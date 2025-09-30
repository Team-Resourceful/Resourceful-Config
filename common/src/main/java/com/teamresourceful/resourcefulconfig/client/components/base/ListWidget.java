package com.teamresourceful.resourcefulconfig.client.components.base;

import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListWidget extends ContainerWidget {

    private static final int SCROLLBAR_WIDTH = 2;
    private static final int SCROLLBAR_PADDING = 4;
    private static final int OVERSCROLL = 2;

    protected final List<Item> items = new ArrayList<>();

    private double scroll = 0;
    private int lastHeight = 0;
    private boolean scrolling = false;

    public ListWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void update(ListWidget old) {
        if (this.items.size() != old.items.size()) return;
        if (this.height != old.height) return;
        updateLastHeight();
        if (this.lastHeight != old.lastHeight) return;

        this.scroll = old.scroll;
        this.scrolling = old.scrolling;
    }

    public void add(Item item) {
        items.add(item);
        updateScrollBar();
    }

    @Override
    public void clear() {
        super.clear();
        this.items.clear();
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return items;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        boolean showsScrollBar = this.lastHeight > this.height;
        int actualWidth = getWidth() - (showsScrollBar ? SCROLLBAR_WIDTH + 4 : 0);

        graphics.enableScissor(getX(), getY(), getX() + actualWidth, getY() + height);

        int y = this.getY() - (int) scroll + OVERSCROLL / 2;
        this.lastHeight = 0;

        for (Item item : items) {
            item.setItemWidth(actualWidth);
            item.setX(getX());
            item.setY(y);

            item.render(graphics, mouseX, mouseY, partialTicks);
            y += item.getHeight();
            this.lastHeight += item.getHeight();
        }

        graphics.disableScissor();

        if (this.lastHeight > this.height) {
            int scrollBarHeight = (int) ((this.height / (double) this.lastHeight) * this.height) - SCROLLBAR_PADDING * 2;
            int scrollBarX = this.getX() + this.width - SCROLLBAR_WIDTH - 1;
            int scrollBarY = this.getY() + SCROLLBAR_PADDING + (int) ((this.scroll / (double) this.lastHeight) * this.height);
            int scrollBarColor = this.isMouseOver(mouseX, mouseY) && mouseX >= scrollBarX && mouseX <= scrollBarX + SCROLLBAR_WIDTH && mouseY >= scrollBarY && mouseY <= scrollBarY + scrollBarHeight ? 0xFFF0F0F0 : 0xFFC0C0C0;
            graphics.fill(scrollBarX, scrollBarY, scrollBarX + SCROLLBAR_WIDTH, scrollBarY + scrollBarHeight, scrollBarColor);
        }
    }

    @Override
    public boolean mouseDragged(@NotNull MouseButtonEvent event, double dragX, double dragY) {
        if (this.scrolling) {
            double scrollBarHeight = (this.height / (double) this.lastHeight) * this.height;
            double scrollBarDragY = dragY / (this.height - scrollBarHeight);
            this.scroll = Mth.clamp(
                    this.scroll + scrollBarDragY * this.lastHeight, 0,
                    Math.max(0, this.lastHeight - this.height + OVERSCROLL)
            );
            return true;
        }
        return super.mouseDragged(event, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.scroll = Mth.clamp(this.scroll - scrollY * 10, 0, Math.max(0, this.lastHeight - this.height + OVERSCROLL));
        return true;
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean bl) {
        if (isMouseOver(event.x(), event.y())) {
            if (isMouseOverScrollBar(event.x(), event.y())) {
                this.scrolling = true;
                return true;
            }
            return super.mouseClicked(event, bl);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(@NotNull MouseButtonEvent event) {
        if (event.input() == 0) {
            this.scrolling = false;
        }
        return super.mouseReleased(event);
    }

    private boolean isMouseOverScrollBar(double mouseX, double mouseY) {
        if (this.lastHeight > this.height) {
            int scrollBarX = this.getX() + this.width - SCROLLBAR_WIDTH - 1;
            return mouseX >= scrollBarX && mouseX <= scrollBarX + SCROLLBAR_WIDTH && mouseY >= this.getY() && mouseY <= this.getY() + this.height;
        }
        return false;
    }

    protected void updateLastHeight() {
        boolean showsScrollBar = this.lastHeight > this.height;
        int actualWidth = getWidth() - (showsScrollBar ? SCROLLBAR_WIDTH + 4 : 0);

        this.lastHeight = 0;
        int y = this.getY() - (int) scroll + OVERSCROLL / 2;
        for (Item item : items) {
            item.setItemWidth(actualWidth);
            item.setX(getX());
            item.setY(y);
            this.lastHeight += item.getHeight();
            y += item.getHeight();
        }
    }

    protected void updateScrollBar() {
        updateLastHeight();
        this.scroll = Mth.clamp(this.scroll, 0, Math.max(0, this.lastHeight - this.height + OVERSCROLL));
    }

    public interface Item extends GuiEventListener, Renderable, NarratableEntry, LayoutElement {

        @Override
        default @NotNull ScreenRectangle getRectangle() {
            return LayoutElement.super.getRectangle();
        }

        void setItemWidth(int width);
    }
}
