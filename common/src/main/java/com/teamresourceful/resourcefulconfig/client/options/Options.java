package com.teamresourceful.resourcefulconfig.client.options;

import it.unimi.dsi.fastutil.doubles.DoubleConsumer;
import it.unimi.dsi.fastutil.doubles.DoubleDoublePair;
import it.unimi.dsi.fastutil.longs.LongConsumer;
import it.unimi.dsi.fastutil.longs.LongLongPair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import com.teamresourceful.resourcefulconfig.common.annotations.*;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;

public final class Options {

    public static AbstractWidget create(int x, int y, int width, ResourcefulConfigEntry entry) {
        if (entry.field().getType().isArray()) {
            Button button = Button.builder(CommonComponents.ELLIPSIS, widget -> {}).bounds(x, y, width, 20).build();
            button.active = false;
            return button;
        }
        return switch (entry.type()) {
            case BOOLEAN -> createBoolean(x, y, width, 20, entry);
            case BYTE, SHORT, INTEGER, LONG -> createInt(x, y, width, 20, entry);
            case FLOAT, DOUBLE -> createFloat(x, y, width, 20, entry);
            case STRING -> new StringInputBox(Minecraft.getInstance().font, x, y, width, 20, entry);
            case ENUM -> createEnum(x, y, width, 20, entry);
        };
    }

    public static CycleButton<Enum<?>> createEnum(int x, int y, int width, int height, ResourcefulConfigEntry entry) {
        if (entry.type() != EntryType.ENUM) {
            throw new IllegalArgumentException("Entry is not an enum!");
        }

        Object values = entry.field().getType().getEnumConstants();

        CycleButton.Builder<Enum<?>> builder = CycleButton.builder(value -> Component.literal(value.name()));
        builder.withValues((Enum<?>[]) values);
        builder.withInitialValue((Enum<?>) ParsingUtils.getField(entry.field()));
        builder.displayOnlyValue();
        return builder.create(x, y, width, height, CommonComponents.EMPTY, (button, value) -> entry.setEnum(value));
    }

    public static CycleButton<Boolean> createBoolean(int x, int y, int width, int height, ResourcefulConfigEntry entry) {
        if (entry.type() != EntryType.BOOLEAN) {
            throw new IllegalArgumentException("Entry is not a boolean!");
        }

        CycleButton.Builder<Boolean> builder = CycleButton.booleanBuilder(CommonComponents.OPTION_ON, CommonComponents.OPTION_OFF);
        builder.withInitialValue((Boolean) ParsingUtils.getField(entry.field()));
        builder.displayOnlyValue();
        return builder.create(x, y, width, height, CommonComponents.EMPTY, (button, value) -> entry.setBoolean(value));
    }

    public static AbstractWidget createInt(int x, int y, int width, int height, ResourcefulConfigEntry entry) {
        if (entry.type() != EntryType.INTEGER && entry.type() != EntryType.BYTE && entry.type() != EntryType.SHORT && entry.type() != EntryType.LONG) {
            throw new IllegalArgumentException("Entry is not an integer!");
        }
        LongConsumer setter = value -> {
            switch (entry.type()) {
                case BYTE -> entry.setByte((byte) value);
                case SHORT -> entry.setShort((short) value);
                case INTEGER -> entry.setInt((int) value);
                case LONG -> entry.setLong(value);
            }
        };
        LongLongPair range = getMinMax(entry);
        if (range == null) {
            return new NumberInputBox(Minecraft.getInstance().font, x, y, width, height, ParsingUtils.getField(entry.field()).toString(), false, setter, null);
        }
        Number number = (Number) ParsingUtils.getField(entry.field());
        return new LongSlider(x, y, width, height, Component.literal(entry.field().getName()), number.longValue(), range.firstLong(), range.secondLong(), setter);
    }

    public static AbstractWidget createFloat(int x, int y, int width, int height, ResourcefulConfigEntry entry) {
        if (entry.type() != EntryType.FLOAT && entry.type() != EntryType.DOUBLE) {
            throw new IllegalArgumentException("Entry is not a float!");
        }
        DoubleConsumer setter = value -> {
            switch (entry.type()) {
                case FLOAT -> entry.setFloat((float) value);
                case DOUBLE -> entry.setDouble(value);
            }
        };
        DoubleDoublePair range = getFloatingMinMax(entry);
        if (range == null) {
            return new NumberInputBox(Minecraft.getInstance().font, x, y, width, height, ParsingUtils.getField(entry.field()).toString(), true, null, setter);
        }
        Number number = (Number) ParsingUtils.getField(entry.field());
        return new DoubleSlider(x, y, width, height, Component.literal(entry.field().getName()), number.doubleValue(), range.firstDouble(), range.secondDouble(), setter);
    }

    private static DoubleDoublePair getFloatingMinMax(ResourcefulConfigEntry entry) {
        switch (entry.type()) {
            case DOUBLE -> {
                DoubleRange range = entry.field().getAnnotation(DoubleRange.class);
                if (range == null) {
                    return null;
                }
                return DoubleDoublePair.of(range.min(), range.max());
            }
            case FLOAT -> {
                FloatRange range = entry.field().getAnnotation(FloatRange.class);
                if (range == null) {
                    return null;
                }
                return DoubleDoublePair.of(range.min(), range.max());
            }
        }
        return null;
    }

    private static LongLongPair getMinMax(ResourcefulConfigEntry entry) {
        return switch (entry.type()) {
            case BYTE -> {
                ByteRange range = entry.field().getAnnotation(ByteRange.class);
                if (range != null) {
                    yield LongLongPair.of(range.min(), range.max());
                }
                yield null;
            }
            case SHORT -> {
                ShortRange range = entry.field().getAnnotation(ShortRange.class);
                if (range != null) {
                    yield LongLongPair.of(range.min(), range.max());
                }
                yield null;
            }
            case INTEGER -> {
                IntRange range = entry.field().getAnnotation(IntRange.class);
                if (range != null) {
                    yield LongLongPair.of(range.min(), range.max());
                }
                yield null;
            }
            case LONG -> {
                LongRange range = entry.field().getAnnotation(LongRange.class);
                if (range != null) {
                    yield LongLongPair.of(range.min(), range.max());
                }
                yield null;
            }
            default -> throw new IllegalArgumentException("Entry is not an integer!");
        };
    }




}
