package com.teamresourceful.resourcefulconfig.client.options;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.LongConsumer;

public class LongSlider extends AbstractSliderButton {

    private final LongConsumer setter;
    private final long min;
    private final long max;
    private long displayValue;

    public LongSlider(int i, int j, int k, int l, Component component, long value, long min, long max, LongConsumer newValue) {
        super(i, j, k, l, component, (value - min) / (double)(max - min));
        this.displayValue = value;
        this.min = min;
        this.max = max;
        this.setter = newValue;
        updateMessage();
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Component.literal(String.format("%d", this.displayValue)));
    }

    @Override
    protected void applyValue() {
        displayValue = Mth.floor(Mth.clampedLerp(min, max, value));
        setter.accept(displayValue);
    }
}
