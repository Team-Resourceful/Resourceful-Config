package com.teamresourceful.resourcefulconfig.api.types.entries;

import com.teamresourceful.resourcefulconfig.api.types.options.Option;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;

import java.lang.reflect.Field;

public interface ResourcefulConfigFieldBackedValueEntry extends ResourcefulConfigValueEntry {

    Field field();

    Object instance();

    default Object get() {
        try {
            return field().get(instance());
        } catch (IllegalAccessException e) {
            return defaultOrElse(null);
        }
    }

    default Object[] getArray() {
        if (!isArray()) return new Object[0];
        try {
            return (Object[]) field().get(instance());
        } catch (IllegalAccessException e) {
            return new Object[0];
        }
    }

    default boolean setArray(Object[] array) {
        try {
            for (Object o : array) {
                if (!type().test(o.getClass())) {
                    return false;
                }
            }
            field().set(instance(), ModUtils.castArray(array, field().getType().componentType()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default byte getByte() {
        if (type() != EntryType.BYTE) return 0;
        try {
            return field().getByte(instance());
        } catch (IllegalAccessException e) {
            return defaultOrElse((byte) 0);
        }
    }

    default boolean setByte(byte value) {
        if (type() != EntryType.BYTE) return false;
        try {
            EntryData options = options();
            if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
                reset();
                return false;
            }
            field().setByte(instance(), value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default short getShort() {
        if (type() != EntryType.SHORT) return 0;
        try {
            return field().getShort(instance());
        } catch (IllegalAccessException e) {
            return defaultOrElse((short) 0);
        }
    }

    default boolean setShort(short value) {
        if (type() != EntryType.SHORT) return false;
        try {
            EntryData options = options();
            if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
                reset();
                return false;
            }
            field().setShort(instance(), value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default int getInt() {
        if (type() != EntryType.INTEGER) return 0;
        try {
            return field().getInt(instance());
        } catch (IllegalAccessException e) {
            return defaultOrElse(0);
        }
    }

    default boolean setInt(int value) {
        if (type() != EntryType.INTEGER) return false;
        try {
            EntryData options = options();
            if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
                reset();
                return false;
            }
            field().setInt(instance(), value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default long getLong() {
        if (type() != EntryType.LONG) return 0;
        try {
            return field().getLong(instance());
        } catch (IllegalAccessException e) {
            return defaultOrElse(0L);
        }
    }

    default boolean setLong(long value) {
        if (type() != EntryType.LONG) return false;
        try {
            EntryData options = options();
            if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
                reset();
                return false;
            }
            field().setLong(instance(), value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default float getFloat() {
        if (type() != EntryType.FLOAT) return 0;
        try {
            return field().getFloat(instance());
        } catch (IllegalAccessException e) {
            return defaultOrElse(0F);
        }
    }

    default boolean setFloat(float value) {
        if (type() != EntryType.FLOAT) return false;
        try {
            EntryData options = options();
            if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
                reset();
                return false;
            }
            field().setFloat(instance(), value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default double getDouble() {
        if (type() != EntryType.DOUBLE) return 0;
        try {
            return field().getDouble(instance());
        } catch (IllegalAccessException e) {
            return defaultOrElse(0D);
        }
    }

    default boolean setDouble(double value) {
        if (type() != EntryType.DOUBLE) return false;
        try {
            EntryData options = options();
            if (options.hasOption(Option.RANGE) && !options.inRange(value)) {
                reset();
                return false;
            }
            field().setDouble(instance(), value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default boolean getBoolean() {
        if (type() != EntryType.BOOLEAN) return false;
        try {
            return field().getBoolean(instance());
        } catch (IllegalAccessException e) {
            return defaultOrElse(false);
        }
    }

    default boolean setBoolean(boolean value) {
        if (type() != EntryType.BOOLEAN) return false;
        try {
            field().setBoolean(instance(), value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default String getString() {
        if (type() != EntryType.STRING) return "";
        try {
            return (String) field().get(instance());
        } catch (IllegalAccessException e) {
            return defaultOrElse("");
        }
    }

    default boolean setString(String value) {
        if (type() != EntryType.STRING) return false;
        if (value == null) return false;
        try {
            EntryData options = options();
            if (options.hasOption(Option.REGEX) && !options.getOption(Option.REGEX).matcher(value).matches()) {
                reset();
                return false;
            }
            field().set(instance(), value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    default Enum<?> getEnum() {
        if (type() != EntryType.ENUM) return null;
        try {
            return (Enum<?>) field().get(instance());
        } catch (IllegalAccessException e) {
            return defaultOrElse(null);
        }
    }

    default boolean setEnum(Enum<?> value) {
        if (type() != EntryType.ENUM) return false;
        if (value == null) return false;
        try {
            field().set(instance(), value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
