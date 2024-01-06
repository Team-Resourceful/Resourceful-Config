package com.teamresourceful.resourcefulconfig.client.components.options.range;

import com.teamresourceful.resourcefulconfig.api.config.EntryOptions;
import com.teamresourceful.resourcefulconfig.api.config.ResourcefulConfigValueEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.LongConsumer;
import java.util.function.LongSupplier;

public record WholeOptionRange(LongConsumer setter, LongSupplier getter, long min, long max, long step) implements OptionRange {

    private WholeOptionRange(LongConsumer setter, LongSupplier getter, double min, double max, long step) {
        this(setter, getter, (long) min, (long) max, step);
    }

    public static WholeOptionRange of(ResourcefulConfigValueEntry entry) {
        return switch (entry.type()) {
            case BYTE -> ofByte(entry);
            case SHORT -> ofShort(entry);
            case INTEGER -> ofInteger(entry);
            case LONG -> ofLong(entry);
            default -> throw new IllegalStateException("Unexpected value: " + entry.type());
        };
    }

    private static WholeOptionRange ofByte(ResourcefulConfigValueEntry entry) {
        final LongConsumer setter = value -> entry.setByte((byte) value);
        final LongSupplier getter = entry::getByte;
        final EntryOptions options = entry.options();
        if (options.hasRange()) {
            return new WholeOptionRange(setter, getter, options.min(), options.max(), 1);
        }
        return new WholeOptionRange(setter, getter, Byte.MIN_VALUE, Byte.MAX_VALUE, 0);
    }

    private static WholeOptionRange ofShort(ResourcefulConfigValueEntry entry) {
        final LongConsumer setter = value -> entry.setShort((short) value);
        final LongSupplier getter = entry::getShort;
        final EntryOptions options = entry.options();
        if (options.hasRange()) {
            return new WholeOptionRange(setter, getter, options.min(), options.max(), 1);
        }
        return new WholeOptionRange(setter, getter, Short.MIN_VALUE, Short.MAX_VALUE, 0);
    }

    private static WholeOptionRange ofInteger(ResourcefulConfigValueEntry entry) {
        final LongConsumer setter = value -> entry.setInt((int) value);
        final LongSupplier getter = entry::getInt;
        final EntryOptions options = entry.options();
        if (options.hasRange()) {
            return new WholeOptionRange(setter, getter, options.min(), options.max(), 1);
        }
        return new WholeOptionRange(setter, getter, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
    }

    private static WholeOptionRange ofLong(ResourcefulConfigValueEntry entry) {
        final LongConsumer setter = entry::setLong;
        final LongSupplier getter = entry::getLong;
        final EntryOptions options = entry.options();
        if (options.hasRange()) {
            return new WholeOptionRange(setter, getter, options.min(), options.max(), 1);
        }
        return new WholeOptionRange(setter, getter, Long.MIN_VALUE, Long.MAX_VALUE, 0);
    }

    @Override
    public Component toComponent() {
        return Component.literal(String.valueOf(this.getter.getAsLong()));
    }

    @Override
    public Component minComponent() {
        return Component.literal(String.valueOf(this.min));
    }

    @Override
    public Component maxComponent() {
        return Component.literal(String.valueOf(this.max));
    }

    @Override
    public void setPercent(double value) {
        this.setter.accept((long) Mth.clampedLerp(min, max, value));
    }

    @Override
    public double getPercent() {
        return (this.getter.getAsLong() - this.min) / (double) (this.max - this.min);
    }

    @Override
    public double getStepPercent() {
        return this.step / (double) (this.max - this.min);
    }

    @Override
    public boolean hasRange() {
        return this.step != 0;
    }

}
