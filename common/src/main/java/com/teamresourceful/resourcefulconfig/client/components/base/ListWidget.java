package com.teamresourceful.resourcefulconfig.client.components.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListWidget extends ContainerWidget {

    private static final int SCROLLBAR_WIDTH = 2;
    private static final int SCROLLBAR_PADDING = 4;
    private static final int OVERSCROLL = 2;

    private final List<Item> items = new ArrayList<>();

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
        updateLastHeight();
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return items;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        boolean showsScrollBar = this.lastHeight > this.height;
        int actualWidth = getWidth() - (showsScrollBar ? SCROLLBAR_WIDTH + 4 : 0);

        graphics.enableScissor(getX() + 1, getY(), getX() + actualWidth, getY() + height);

        int y = this.getY() - (int) scroll + OVERSCROLL / 2;
        this.lastHeight = 0;

        for (Item item : items) {
            item.setWidth(actualWidth);
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
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling) {
            double scrollBarHeight = (this.height / (double) this.lastHeight) * this.height;
            double scrollBarDragY = dragY / (this.height - scrollBarHeight);
            this.scroll = Mth.clamp(
                    this.scroll + scrollBarDragY * this.lastHeight, 0,
                    Math.max(0, this.lastHeight - this.height + OVERSCROLL)
            );
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.scroll = Mth.clamp(this.scroll - scrollY * 10, 0, Math.max(0, this.lastHeight - this.height + OVERSCROLL));
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            if (isMouseOverScrollBar(mouseX, mouseY)) {
                this.scrolling = true;
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        if (i == 0) {
            this.scrolling = false;
        }
        return super.mouseReleased(d, e, i);
    }

    private boolean isMouseOverScrollBar(double mouseX, double mouseY) {
        if (this.lastHeight > this.height) {
            int scrollBarX = this.getX() + this.width - SCROLLBAR_WIDTH - 1;
            return mouseX >= scrollBarX && mouseX <= scrollBarX + SCROLLBAR_WIDTH && mouseY >= this.getY() && mouseY <= this.getY() + this.height;
        }
        return false;
    }

    private void updateLastHeight() {
        boolean showsScrollBar = this.lastHeight > this.height;
        int actualWidth = getWidth() - (showsScrollBar ? SCROLLBAR_WIDTH + 4 : 0);

        this.lastHeight = 0;
        int y = this.getY() - (int) scroll + OVERSCROLL / 2;
        for (Item item : items) {
            item.setWidth(actualWidth);
            item.setX(getX());
            item.setY(y);
            this.lastHeight += item.getHeight();
            y += item.getHeight();
        }
    }

    public interface Item extends GuiEventListener, Renderable, NarratableEntry, LayoutElement {

        @Override
        default @NotNull ScreenRectangle getRectangle() {
            return LayoutElement.super.getRectangle();
        }

        void setWidth(int width);
    }
}
