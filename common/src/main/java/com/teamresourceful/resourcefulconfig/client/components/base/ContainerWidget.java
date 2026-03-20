package com.teamresourceful.resourcefulconfig.client.components.base;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    protected void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        for (Renderable renderable : renderables) {
            renderable.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (this.focused != null) {
            this.focused.setFocused(focused);
        }
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
        if (this.focused != null && this.focused != guiEventListener) {
            this.focused.setFocused(false);
        }

        if (guiEventListener != null) {
            guiEventListener.setFocused(true);
        }

        this.focused = guiEventListener;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput output) {

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


    @Override public void onClick(@NotNull MouseButtonEvent event, boolean bl) {}
    @Override public void onRelease(@NotNull MouseButtonEvent event) {}
    @Override public void onDrag(@NotNull MouseButtonEvent event, double dragX, double dragY) {}

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean bl) {
        Optional<GuiEventListener> optional = this.getChildAt(event.x(), event.y());
        if (optional.isPresent()) {
            GuiEventListener guiEventListener = optional.get();
            if (guiEventListener.mouseClicked(event, bl)) {
                this.setFocused(guiEventListener);
                if (event.input() == 0) {
                    this.setDragging(true);
                }

                return true;
            }
        }
        return false;
    }


    @Override
    public boolean mouseReleased(@NotNull MouseButtonEvent event) {
        return ContainerEventHandler.super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(@NotNull MouseButtonEvent event, double dragX, double dragY) {
        return ContainerEventHandler.super.mouseDragged(event, dragX, dragY);
    }
}