package com.teamresourceful.resourcefulconfig.client.options;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.DoubleConsumer;

public class DoubleSlider extends AbstractSliderButton {

    private final DoubleConsumer setter;
    private final double min;
    private final double max;
    private double displayValue;

    public DoubleSlider(int i, int j, int k, int l, Component component, double value, double min, double max, DoubleConsumer newValue) {
        super(i, j, k, l, component, (value - min) / (max - min));
        this.displayValue = value;
        this.min = min;
        this.max = max;
        this.setter = newValue;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format("%.2f", this.displayValue)));
    }

    @Override
    protected void applyValue() {
        displayValue = (double) Math.round(Mth.clampedLerp(min, max, value) * 100) / 100;
        setter.accept(displayValue);
    }
}
