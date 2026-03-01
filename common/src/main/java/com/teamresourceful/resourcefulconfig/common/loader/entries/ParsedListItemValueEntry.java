package com.teamresourceful.resourcefulconfig.common.loader.entries;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import com.teamresourceful.resourcefulconfig.api.types.options.Option;

import java.util.List;

public record ParsedListItemValueEntry(
        EntryType type,
        EntryData options,
        Class<?> objectType,
        List<Object> list,
        int index,
        Object defaultValue
) implements ResourcefulConfigValueEntry {

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public void reset() {
        list.set(index, defaultValue);
    }

    @Override
    public Object get() {
        return list.get(index);
    }

    @Override
    public Object[] getArray() {
        return new Object[0];
    }

    @Override
    public boolean setArray(Object[] array) {
        return false;
    }

    @Override
    public byte getByte() {
        return type == EntryType.BYTE ? ((Number) list.get(index)).byteValue() : 0;
    }

    @Override
    public boolean setByte(byte value) {
        if (type != EntryType.BYTE) return false;
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) { reset(); return false; }
        list.set(index, value);
        return true;
    }

    @Override
    public short getShort() {
        return type == EntryType.SHORT ? ((Number) list.get(index)).shortValue() : 0;
    }

    @Override
    public boolean setShort(short value) {
        if (type != EntryType.SHORT) return false;
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) { reset(); return false; }
        list.set(index, value);
        return true;
    }

    @Override
    public int getInt() {
        return type == EntryType.INTEGER ? ((Number) list.get(index)).intValue() : 0;
    }

    @Override
    public boolean setInt(int value) {
        if (type != EntryType.INTEGER) return false;
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) { reset(); return false; }
        list.set(index, value);
        return true;
    }

    @Override
    public long getLong() {
        return type == EntryType.LONG ? ((Number) list.get(index)).longValue() : 0;
    }

    @Override
    public boolean setLong(long value) {
        if (type != EntryType.LONG) return false;
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) { reset(); return false; }
        list.set(index, value);
        return true;
    }

    @Override
    public float getFloat() {
        return type == EntryType.FLOAT ? ((Number) list.get(index)).floatValue() : 0;
    }

    @Override
    public boolean setFloat(float value) {
        if (type != EntryType.FLOAT) return false;
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) { reset(); return false; }
        list.set(index, value);
        return true;
    }

    @Override
    public double getDouble() {
        return type == EntryType.DOUBLE ? ((Number) list.get(index)).doubleValue() : 0;
    }

    @Override
    public boolean setDouble(double value) {
        if (type != EntryType.DOUBLE) return false;
        if (options.hasOption(Option.RANGE) && !options.inRange(value)) { reset(); return false; }
        list.set(index, value);
        return true;
    }

    @Override
    public boolean getBoolean() {
        return type == EntryType.BOOLEAN && (Boolean) list.get(index);
    }

    @Override
    public boolean setBoolean(boolean value) {
        if (type != EntryType.BOOLEAN) return false;
        list.set(index, value);
        return true;
    }

    @Override
    public String getString() {
        return type == EntryType.STRING ? (String) list.get(index) : "";
    }

    @Override
    public boolean setString(String value) {
        if (type != EntryType.STRING || value == null) return false;
        if (options.hasOption(Option.REGEX) && !options.getOption(Option.REGEX).matcher(value).matches()) {
            reset();
            return false;
        }
        list.set(index, value);
        return true;
    }

    @Override
    public Enum<?> getEnum() {
        return type == EntryType.ENUM ? (Enum<?>) list.get(index) : null;
    }

    @Override
    public boolean setEnum(Enum<?> value) {
        if (type != EntryType.ENUM || value == null) return false;
        list.set(index, value);
        return true;
    }
}