package com.teamresourceful.resourcefulconfig.common.config.forge;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import com.teamresourceful.resourcefulconfig.common.annotations.*;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ForgeConfigUtils {

    @SuppressWarnings("unchecked")
    public static <T> void setEntry(ForgeConfigSpec.ConfigValue<T> value, Object object) {
        if (object.getClass().isArray()) {
            value.set((T) new ArrayList<>(List.of((Object[]) object)));
        } else {
            value.set((T) object);
        }
    }

    public static ForgeConfigSpec.ConfigValue<?> addEntry(ForgeConfigSpec.Builder builder, String id, Field field, EntryType type, Object defaultValue) {
        boolean isArray = field.getType().isArray();
        return switch (type) {
            case BOOLEAN -> builder.define(id, (boolean)defaultValue);
            case BYTE -> {
                ByteRange range = field.getAnnotation(ByteRange.class);
                if (isArray) {
                    yield builder.defineList(id, ParsingUtils.<Byte>toList(defaultValue), o -> o instanceof Byte value && (range == null || (value >= range.min() && value <= range.max())));
                } else {
                    byte value = (byte)defaultValue;
                    if (range != null) {
                        if (value < range.min() || value > range.max()) {
                            throw new IllegalArgumentException("Default value for " + id + " is out of range");
                        }
                        yield builder.defineInRange(id, value, range.min(), range.max());
                    } else {
                        yield defineNumber(id, value, Byte.class, builder);
                    }
                }
            }
            case SHORT -> {
                ShortRange range = field.getAnnotation(ShortRange.class);
                if (isArray) {
                    yield builder.defineList(id, ParsingUtils.<Short>toList(defaultValue), o -> o instanceof Short value && (range == null || (value >= range.min() && value <= range.max())));
                } else {
                    short value = (short) defaultValue;
                    if (range != null) {
                        if (value < range.min() || value > range.max()) {
                            throw new IllegalArgumentException("Default value for " + id + " is out of range");
                        }
                        yield builder.defineInRange(id, value, range.min(), range.max());
                    } else {
                        yield defineNumber(id, value, Short.class, builder);
                    }
                }
            }
            case INTEGER -> {
                IntRange range = field.getAnnotation(IntRange.class);
                if (isArray) {
                    yield builder.defineList(id, ParsingUtils.<Integer>toList(defaultValue), o -> o instanceof Integer value && (range == null || (value >= range.min() && value <= range.max())));
                } else {
                    int value = (int) defaultValue;
                    if (range != null) {
                        if (value < range.min() || value > range.max()) {
                            throw new IllegalArgumentException("Default value for " + id + " is out of range");
                        }
                        yield builder.defineInRange(id, value, range.min(), range.max());
                    } else {
                        yield defineNumber(id, value, Integer.class, builder);
                    }
                }
            }
            case LONG -> {
                LongRange range = field.getAnnotation(LongRange.class);
                if (isArray) {
                    yield builder.defineList(id, ParsingUtils.<Long>toList(defaultValue), o -> o instanceof Long value && (range == null || (value >= range.min() && value <= range.max())));
                } else {
                    long value = (long) defaultValue;
                    if (range != null) {
                        if (value < range.min() || value > range.max()) {
                            throw new IllegalArgumentException("Default value for " + id + " is out of range");
                        }
                        yield builder.defineInRange(id, value, range.min(), range.max());
                    } else {
                        yield defineNumber(id, value, Long.class, builder);
                    }
                }
            }
            case FLOAT -> {
                FloatRange range = field.getAnnotation(FloatRange.class);
                if (isArray) {
                    yield builder.defineList(id, ParsingUtils.<Float>toList(defaultValue), o -> o instanceof Float value && (range == null || (value >= range.min() && value <= range.max())));
                } else {
                    float value = (float) defaultValue;
                    if (range != null) {
                        if (value < range.min() || value > range.max()) {
                            throw new IllegalArgumentException("Default value for " + id + " is out of range");
                        }
                        yield builder.defineInRange(id, value, range.min(), range.max());
                    } else {
                        yield defineNumber(id, value, Float.class, builder);
                    }
                }
            }
            case DOUBLE -> {
                DoubleRange range = field.getAnnotation(DoubleRange.class);
                if (isArray) {
                    yield builder.defineList(id, ParsingUtils.<Double>toList(defaultValue), o -> o instanceof Double value && (range == null || (value >= range.min() && value <= range.max())));
                } else {
                    double value = (double) defaultValue;
                    if (range != null) {
                        if (value < range.min() || value > range.max()) {
                            throw new IllegalArgumentException("Default value for " + id + " is out of range");
                        }
                        yield builder.defineInRange(id, value, range.min(), range.max());
                    } else {
                        yield defineNumber(id, value, Double.class, builder);
                    }
                }
            }
            case STRING -> {
                if (isArray) {
                    yield builder.defineList(id, ParsingUtils.<String>toList(defaultValue), o -> o instanceof String);
                } else {
                    yield builder.define(id, defaultValue);
                }
            }
            case ENUM -> {
                if (isArray) {
                    throw new RuntimeException("Arrays of enums are not supported");
                } else {
                    //noinspection unchecked, rawtypes
                    yield builder.defineEnum(id, (Enum) defaultValue);
                }
            }
        };
    }

    public static ForgeConfigSpec.ConfigValue<?> defineNumber(String id, Number defaultValue, Class<? extends Number> clazz, ForgeConfigSpec.Builder builder) {
        return builder.define(Lists.newArrayList(Splitter.on(".").split(id)), () -> defaultValue, o -> o instanceof Number, clazz);
    }
}
