package com.teamresourceful.resourcefulconfig.client.components.options.types;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefulconfig.client.components.ModSprites;
import com.teamresourceful.resourcefulconfig.client.components.base.BaseWidget;
import com.teamresourceful.resourcefulconfig.client.components.options.range.OptionRange;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

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
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        updateIfFocused();

        graphics.blitSprite(ModSprites.BUTTON, getX(), getY(), this.width, this.height);
        graphics.blitSprite(ModSprites.BUTTON_HOVER, getX() + this.padding, getY() + 5, this.width - this.padding * 2, this.height - 10);

        int sliderX = getX() + this.padding + (int) ((this.width - this.padding * 2) * this.getter.getAsDouble()) - (this.height - 6) / 2;
        graphics.blitSprite(ModSprites.CONTAINER, sliderX, getY() + 4, this.height - 8, this.height - 8);

        Component tooltip = null;

        if (mouseX >= getX() + this.padding && mouseX <= getX() + this.width - this.padding && mouseY >= getY() + 4 && mouseY <= getY() + this.height - 4) {
            tooltip = this.display.get();
        }

        if (isHoveredOrFocused()) {

            renderScrollingString(
                    graphics, font,
                    this.minDisplay,
                    getX() + 2, getY() + 2,
                    getX() + this.padding - 2, getY() + this.height - 2,
                    0xFFFFFF
            );

            renderScrollingString(
                    graphics, font,
                    this.maxDisplay,
                    getX() + this.width - this.padding + 2, getY() + 2,
                    getX() + this.width - 2, getY() + this.height - 2,
                    0xFFFFFF
            );

            if (mouseX >= getX() + 2 && mouseX <= getX() + this.padding - 2) {
                tooltip = this.minDisplay;
            } else if (mouseX >= getX() + this.width - this.padding + 2 && mouseX <= getX() + this.width - 2) {
                tooltip = this.maxDisplay;
            }
        }

        if (tooltip != null && Minecraft.getInstance().screen != null && this.isHovered()) {
            Minecraft.getInstance().screen.setTooltipForNextRenderPass(
                    List.of(tooltip.getVisualOrderText())
            );
        }
    }

    public void updateIfFocused() {
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
    public boolean mouseClicked(double d, double e, int i) {
        this.setValueFromMouse(d);
        return true;
    }

    @Override
    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        this.setValueFromMouse(d);
        return super.mouseDragged(d, e, i, f, g);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (CommonInputs.selected(i)) {
            this.canChangeValue = !this.canChangeValue;
            return true;
        } else {
            if (this.canChangeValue) {
                boolean leftArrow = i == InputConstants.KEY_LEFT;
                if (leftArrow || i == InputConstants.KEY_RIGHT) {
                    double step = leftArrow ? -this.step : this.step;
                    step *= Screen.hasShiftDown() ? 10 : 1;
                    double value = this.getter.getAsDouble() + step / (float) (this.width - 8);
                    this.setter.accept(Mth.clamp(value, 0.0D, 1.0D));
                    return true;
                }
            }

            return false;
        }
    }

    private void setValueFromMouse(double mouseX) {
        if (!this.isFocused()) return;
        double correctX = mouseX - (this.getX() + PADDING);
        double value = correctX / (double) (this.getWidth() - PADDING * 2);
        this.setter.accept(Mth.clamp(value, 0.0D, 1.0D));
    }


}
