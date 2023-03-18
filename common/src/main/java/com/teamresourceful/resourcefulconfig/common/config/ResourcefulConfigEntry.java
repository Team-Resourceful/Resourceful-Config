package com.teamresourceful.resourcefulconfig.common.config;

import com.teamresourceful.resourcefulconfig.common.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Objects;

public interface ResourcefulConfigEntry {

    EntryType type();

    Field field();

    Object defaultValue();

    @SuppressWarnings("unchecked")
    default <T> T getDefaultOrElse(T value) {
        final Object defaultValue = defaultValue();
        return defaultValue == null ? value : (T) defaultValue;
    }

    default <T extends Annotation> T getAnnotation(Class<T> annotation) {
        return field().getAnnotation(annotation);
    }

    default boolean setArray(Object[] array) {
        try {
            for (Object o : array) {
                if (!type().test(o.getClass())) {
                    return false;
                }
            }
            field().set(null, castArray(array, field().getType().componentType()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] castArray(Object[] array, Class<T> clazz) {
        T[] newArray = (T[]) Array.newInstance(clazz, array.length);
        for (int i = 0; i < array.length; i++) {
            newArray[i] = clazz.cast(array[i]);
        }
        return newArray;
    }



    default boolean setByte(byte value) {
        if (type() != EntryType.BYTE) return false;
        try {
            ByteRange range = field().getAnnotation(ByteRange.class);
            if (range != null && (value < range.min() || value > range.max())) {
                field().setByte(null, (Byte) defaultValue());
                return false;
            }
            field().setByte(null, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean setShort(short value) {
        if (type() != EntryType.SHORT) return false;
        try {
            ShortRange range = field().getAnnotation(ShortRange.class);
            if (range != null && (value < range.min() || value > range.max())) {
                field().setShort(null, (Short) defaultValue());
                return false;
            }
            field().setShort(null, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean setInt(int value) {
        if (type() != EntryType.INTEGER) return false;
        try {
            IntRange range = field().getAnnotation(IntRange.class);
            if (range != null && (value < range.min() || value > range.max())) {
                field().setInt(null, (Integer) defaultValue());
                return false;
            }
            field().setInt(null, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean setLong(long value) {
        if (type() != EntryType.LONG) return false;
        try {
            LongRange range = field().getAnnotation(LongRange.class);
            if (range != null && (value < range.min() || value > range.max())) {
                field().setLong(null, (Long) defaultValue());
                return false;
            }
            field().setLong(null, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean setFloat(float value) {
        if (type() != EntryType.FLOAT) return false;
        try {
            FloatRange range = field().getAnnotation(FloatRange.class);
            if (range != null && (value < range.min() || value > range.max())) {
                field().setFloat(null, (Float) defaultValue());
                return false;
            }
            field().setFloat(null, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean setDouble(double value) {
        if (type() != EntryType.DOUBLE) return false;
        try {
            DoubleRange range = field().getAnnotation(DoubleRange.class);
            if (range != null && (value < range.min() || value > range.max())) {
                field().setDouble(null, (Double) defaultValue());
                return false;
            }
            field().setDouble(null, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean setBoolean(boolean value) {
        if (type() != EntryType.BOOLEAN) return false;
        try {
            field().setBoolean(null, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean setString(String value) {
        if (type() != EntryType.STRING) return false;
        try {
            field().set(null, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean setEnum(Enum<?> value) {
        if (type() != EntryType.ENUM) return false;
        if (value == null) return false;
        try {
            field().set(null, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean isDefault(Object object) {
        return switch (type()) {
            case BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, BOOLEAN, STRING -> Objects.equals(object, defaultValue());
            case ENUM -> object instanceof Enum<?> value && value.name().equals(((Enum<?>) defaultValue()).name());
        };
    }

    default void reset() {
        if (field().getType().isArray()) {
            setArray((Object[]) defaultValue());
        } else {
            switch (type()) {
                case BYTE -> setByte((Byte) defaultValue());
                case SHORT -> setShort((Short) defaultValue());
                case INTEGER -> setInt((Integer) defaultValue());
                case LONG -> setLong((Long) defaultValue());
                case FLOAT -> setFloat((Float) defaultValue());
                case DOUBLE -> setDouble((Double) defaultValue());
                case BOOLEAN -> setBoolean((Boolean) defaultValue());
                case STRING -> setString((String) defaultValue());
                case ENUM -> setEnum((Enum<?>) defaultValue());
            }
        }
    }
}
