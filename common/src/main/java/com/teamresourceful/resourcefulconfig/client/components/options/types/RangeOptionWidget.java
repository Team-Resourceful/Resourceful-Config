package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.range.OptionRange;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class RangeOptionWidget extends BaseWidget {

    private static final int FOCUSED_EXTRA_WIDTH = 40;
    private static final int WIDTH = 80;
    private static final int FOCUSED_WIDTH = WIDTH + FOCUSED_EXTRA_WIDTH;
    private static final int PADDING = 20;

    private final Supplier<Component> display;
    private final Component minDisplay;
    private final Component maxDisplay;
    private final DoubleSupplier getter;
    private final DoubleConsumer setter;
    private final double step;
    private final boolean canBeFocused;

    private int padding = 5;
    private boolean canChangeValue;

    public RangeOptionWidget(OptionRange range) {
        this(range::toComponent, range.minComponent(), range.maxComponent(), range::getPercent, range::setPercent, range.getStepPercent());
    }

    public RangeOptionWidget(
        Supplier<Component> display, Component minDisplay, Component maxDisplay,
        DoubleSupplier getter, DoubleConsumer setter, Double step
    ) {
        super(WIDTH, 16);

        this.display = display;
        this.minDisplay = minDisplay;
        this.maxDisplay = maxDisplay;
        this.getter = getter;
        this.setter = setter;
        this.step = step;
        this.canBeFocused = this.minDisplay != null && this.maxDisplay != null;
    }

    @Override
    public void applyCursor(@NotNull GuiGraphicsExtractor graphics) {
        if (!this.isHovered()) return;
        graphics.requestCursor(this.isActive() ? CursorTypes.RESIZE_EW : CursorTypes.NOT_ALLOWED);
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        updateIfFocused();

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.BUTTON, getX(), getY(), this.width, this.height);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.BUTTON_HOVER, getX() + this.padding, getY() + 5, this.width - this.padding * 2, this.height - 10);

        int sliderX = getX() + this.padding + (int) ((this.width - this.padding * 2) * this.getter.getAsDouble()) - (this.height - 6) / 2;
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ModSprites.CONTAINER, sliderX, getY() + 4, this.height - 8, this.height - 8);

        Component tooltip = null;

        if (mouseX >= getX() + this.padding && mouseX <= getX() + this.width - this.padding && mouseY >= getY() + 4 && mouseY <= getY() + this.height - 4) {
            tooltip = this.display.get();
            this.applyCursor(graphics);
        }

        if (isHoveredOrFocused() && this.canBeFocused) {
            var renderer = graphics.textRendererForWidget(
                    this,
                    GuiGraphicsExtractor.HoveredTextEffects.NONE
            );
            renderer.acceptScrollingWithDefaultCenter(
                    this.minDisplay,
                    getX() + 2, getX() + this.padding - 2,
                    getY() + 2, getY() + this.height - 2
            );

            renderer.acceptScrollingWithDefaultCenter(
                    this.maxDisplay,
                    getX() + this.width - this.padding + 2, getX() + this.width - 2,
                    getY() + this.height - 2, getY() + 2
            );

            if (mouseX >= getX() + 2 && mouseX <= getX() + this.padding - 2) {
                tooltip = this.minDisplay;
            } else if (mouseX >= getX() + this.width - this.padding + 2 && mouseX <= getX() + this.width - 2) {
                tooltip = this.maxDisplay;
            }
        }

        if (tooltip != null && Minecraft.getInstance().screen != null && this.isHovered()) {
            graphics.setTooltipForNextFrame(
                    List.of(tooltip.getVisualOrderText()),
                    mouseX, mouseY
            );
        }
    }

    public void updateIfFocused() {
        if (!this.canBeFocused) return;
        if (this.width != FOCUSED_WIDTH && isHoveredOrFocused()) {
            setWidth(FOCUSED_WIDTH);
            setX(getX() - FOCUSED_EXTRA_WIDTH);
            this.padding = PADDING;
        } else if (this.width != WIDTH && !isHoveredOrFocused()) {
            setWidth(WIDTH);
            setX(getX() + FOCUSED_EXTRA_WIDTH);
            this.padding = PADDING / 4;
        }
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (!focused) {
            this.canChangeValue = false;
        } else {
            InputType type = Minecraft.getInstance().getLastInputType();
            if (type.isMouse() || type == InputType.KEYBOARD_TAB) {
                this.canChangeValue = true;
            }
        }
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean bl) {
        if (this.isHovered()) {
            this.setValueFromMouse(event.x());
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(@NotNull MouseButtonEvent event, double dragX, double dragY) {
        if (this.isHovered() && event.button() == 0) {
            this.setValueFromMouse(event.x());
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isSelection()) {
            this.canChangeValue = !this.canChangeValue;
            return true;
        } else {
            if (this.canChangeValue) {
                boolean leftArrow = event.input() == InputConstants.KEY_LEFT;
                if (leftArrow || event.input() == InputConstants.KEY_RIGHT) {
                    double step = leftArrow ? -this.step : this.step;
                    step *= event.hasShiftDown() ? 10 : 1;
                    double value = (this.getter.getAsDouble() + step) / (float) (this.width - 8);
                    this.setter.accept(Mth.clamp(value, 0.0D, 1.0D));
                    return true;
                }
            }

            return false;
        }
    }

    private void setValueFromMouse(double mouseX) {
        if (!this.isFocused()) return;
        double correctX = mouseX - (this.getX() + this.padding);
        double value = correctX / (double) (this.getWidth() - this.padding * 2);
        this.setter.accept(Mth.clamp(value, 0.0D, 1.0D));
    }


}
