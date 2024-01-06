package com.teamresourceful.resourcefulconfig.client.components.base;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ContainerWidget extends AbstractWidget implements ContainerEventHandler {

    protected final List<Renderable> renderables = new ArrayList<>();
    protected final List<GuiEventListener> children = new ArrayList<>();

    @Nullable
    private GuiEventListener focused;
    private boolean isDragging;

    public ContainerWidget(int x, int y, int width, int height) {
        super(x, y, width, height, CommonComponents.EMPTY);
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return children;
    }

    protected <T extends GuiEventListener & Renderable> T addRenderableWidget(T widget) {
        this.renderables.add(widget);
        this.children.add(widget);
        return widget;
    }

    protected void clear() {
        this.renderables.clear();
        this.children.clear();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (Renderable renderable : renderables) {
            renderable.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
    }

    @Override
    public final boolean isDragging() {
        return this.isDragging;
    }

    @Override
    public final void setDragging(boolean bl) {
        this.isDragging = bl;
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener) {
        if (this.focused != null) {
            this.focused.setFocused(false);
        }

        if (guiEventListener != null) {
            guiEventListener.setFocused(true);
        }

        this.focused = guiEventListener;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void setX(int i) {
        int oldX = this.getX();
        super.setX(i);
        if (oldX != i) {
            positionUpdated();
        }
    }

    @Override
    public void setY(int i) {
        int oldY = this.getY();
        super.setY(i);
        if (oldY != i) {
            positionUpdated();
        }
    }

    protected void positionUpdated() {

    }

    // Buttons

    @Override public final void onClick(double d, double e) {}
    @Override public final void onRelease(double d, double e) {}
    @Override protected final void onDrag(double d, double e, double f, double g) {}

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        return ContainerEventHandler.super.mouseClicked(d, e, i);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        return ContainerEventHandler.super.mouseReleased(d, e, i);
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        return ContainerEventHandler.super.mouseDragged(d, e, i, f, g);
    }
}