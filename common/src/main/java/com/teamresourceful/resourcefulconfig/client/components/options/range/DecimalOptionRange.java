package com.teamresourceful.resourcefulconfig.client.components.options.range;

import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.api.types.options.Option;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public record DecimalOptionRange(DoubleConsumer setter, DoubleSupplier getter, double min, double max, double step) implements OptionRange {

    public static DecimalOptionRange of(ResourcefulConfigValueEntry entry) {
        return switch (entry.type()) {
            case FLOAT -> ofFloat(entry);
            case DOUBLE -> ofDouble(entry);
            default -> throw new IllegalStateException("Unexpected value: " + entry.type());
        };
    }

    private static DecimalOptionRange ofFloat(ResourcefulConfigValueEntry entry) {
        final DoubleConsumer setter = value -> entry.setFloat((float) value);
        final DoubleSupplier getter = entry::getFloat;
        final EntryData options = entry.options();
        if (options.hasOption(Option.RANGE)) {
            ConfigOption.Range range = options.getOption(Option.RANGE);
            return new DecimalOptionRange(setter, getter, range.min(), range.max(), 1);
        }
        return new DecimalOptionRange(setter, getter, Float.MIN_VALUE, Float.MAX_VALUE, 0);
    }

    private static DecimalOptionRange ofDouble(ResourcefulConfigValueEntry entry) {
        final DoubleConsumer setter = entry::setDouble;
        final DoubleSupplier getter = entry::getDouble;
        final EntryData options = entry.options();
        if (options.hasOption(Option.RANGE)) {
            ConfigOption.Range range = options.getOption(Option.RANGE);
            return new DecimalOptionRange(setter, getter, range.min(), range.max(), 1);
        }
        return new DecimalOptionRange(setter, getter, Double.MIN_VALUE, Double.MAX_VALUE, 0);
    }

    @Override
    public Component toComponent() {
        return Component.literal(String.format("%.2f", this.getter.getAsDouble()));
    }

    @Override
    public Component minComponent() {
        return Component.literal(String.format("%.2f", this.min));
    }

    @Override
    public Component maxComponent() {
        return Component.literal(String.format("%.2f", this.max));
    }

    @Override
    public void setPercent(double value) {
        this.setter.accept(Mth.clampedLerp(min, max, value));
    }

    @Override
    public double getPercent() {
        return (this.getter.getAsDouble() - min) / (max - min);
    }

    @Override
    public double getStepPercent() {
        return this.step / (max - min);
    }

    @Override
    public boolean hasRange() {
        return this.step != 0;
    }
}
