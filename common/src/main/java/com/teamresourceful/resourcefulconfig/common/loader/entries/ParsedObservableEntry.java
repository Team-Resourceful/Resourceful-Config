package com.teamresourceful.resourcefulconfig.common.loader.entries;

import com.teamresourceful.resourcefulconfig.api.types.entries.Observable;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.Option;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;

import java.lang.reflect.Field;

public record ParsedObservableEntry(
    EntryType type,
    Class<?> objectType,
    Observable<?> observable,
    EntryData options,
    Object defaultValue
) implements ResourcefulConfigValueEntry {

    public static ParsedObservableEntry of(EntryType type, Field field, Object instance) {
        Observable<?> observable = (Observable<?>) ParsingUtils.getField(field, instance);
        Object defaultValue = observable.get();
        return new ParsedObservableEntry(
                type,
                observable.type().isArray() ? observable.type().getComponentType() : observable.type(),
                observable,
                EntryData.of(field, observable.type()),
                defaultValue
        );
    }

    @Override
    public boolean isArray() {
        return observable.type().isArray();
    }

    @Override
    public Object get() {
        return observable.get();
    }

    @Override
    public Object[] getArray() {
        if (!isArray()) return new Object[0];
        return (Object[]) observable.get();
    }

    @Override
    public boolean setArray(Object[] array) {
        for (Object o : array) {
            if (!type().test(o.getClass())) {
                return false;
            }
        }
        observable.setAndCast(ModUtils.castArray(array, objectType));
        return true;
    }

    @Override
    public byte getByte() {
        Object value = get();
        return value instanceof Byte ? (byte) value : defaultOrElse((byte) 0);
    }

    @Override
    public boolean setByte(byte value) {
        if (type != EntryType.BYTE) return false;
        EntryData options = options();
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
            reset();
            return false;
        }
        observable.setAndCast(value);
        return true;
    }

    @Override
    public short getShort() {
        Object value = get();
        return value instanceof Short ? (short) value : defaultOrElse((short) 0);
    }

    @Override
    public boolean setShort(short value) {
        if (type != EntryType.SHORT) return false;
        EntryData options = options();
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
            reset();
            return false;
        }
        observable.setAndCast(value);
        return true;
    }

    @Override
    public int getInt() {
        Object value = get();
        return value instanceof Integer ? (int) value : defaultOrElse(0);
    }

    @Override
    public boolean setInt(int value) {
        if (type != EntryType.INTEGER) return false;
        EntryData options = options();
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
            reset();
            return false;
        }
        observable.setAndCast(value);
        return true;
    }

    @Override
    public long getLong() {
        Object value = get();
        return value instanceof Long ? (long) value : defaultOrElse(0L);
    }

    @Override
    public boolean setLong(long value) {
        if (type != EntryType.LONG) return false;
        EntryData options = options();
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
            reset();
            return false;
        }
        observable.setAndCast(value);
        return true;
    }

    @Override
    public float getFloat() {
        Object value = get();
        return value instanceof Float ? (float) value : defaultOrElse(0.0f);
    }

    @Override
    public boolean setFloat(float value) {
        if (type != EntryType.FLOAT) return false;
        EntryData options = options();
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
            reset();
            return false;
        }
        observable.setAndCast(value);
        return true;
    }

    @Override
    public double getDouble() {
        Object value = get();
        return value instanceof Double ? (double) value : defaultOrElse(0.0);
    }

    @Override
    public boolean setDouble(double value) {
        if (type != EntryType.DOUBLE) return false;
        EntryData options = options();
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
            reset();
            return false;
        }
        observable.setAndCast(value);
        return true;
    }

    @Override
    public boolean getBoolean() {
        Object value = get();
        return value instanceof Boolean bool ? bool : defaultOrElse(false);
    }

    @Override
    public boolean setBoolean(boolean value) {
        if (type != EntryType.BOOLEAN) return false;
        observable.setAndCast(value);
        return true;
    }

    @Override
    public String getString() {
        Object value = get();
        return value instanceof String ? (String) value : defaultOrElse("");
    }

    @Override
    public boolean setString(String value) {
        if (type != EntryType.STRING) return false;
        observable.setAndCast(value);
        return true;
    }

    @Override
    public Enum<?> getEnum() {
        Object value = get();
        return value instanceof Enum<?> ? (Enum<?>) value : null;
    }

    @Override
    public boolean setEnum(Enum<?> value) {
        if (type != EntryType.ENUM) return false;
        observable.setAndCast(value);
        return true;
    }

    @Override
    public void reset() {
        if (isArray()) {
            setArray((Object[]) defaultValue);
        } else {
            switch (type) {
                case BYTE -> setByte((byte) defaultValue);
                case SHORT -> setShort((short) defaultValue);
                case INTEGER -> setInt((int) defaultValue);
                case LONG -> setLong((long) defaultValue);
                case FLOAT -> setFloat((float) defaultValue);
                case DOUBLE -> setDouble((double) defaultValue);
                case BOOLEAN -> setBoolean((boolean) defaultValue);
                case STRING -> setString((String) defaultValue);
                case ENUM -> setEnum((Enum<?>) defaultValue);
                case OBJECT -> throw new IllegalStateException("Object cannot be in a value entry!");
            }
        }
    }
}
