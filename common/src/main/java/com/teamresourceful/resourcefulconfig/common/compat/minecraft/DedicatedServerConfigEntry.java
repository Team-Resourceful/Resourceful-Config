package com.teamresourceful.resourcefulconfig.common.compat.minecraft;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import net.minecraft.server.dedicated.DedicatedServer;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DedicatedServerConfigEntry<T> implements ResourcefulConfigValueEntry {

    private final EntryType type;
    private final EntryData options;

    private final T defaultValue;

    private final Supplier<T> getter;
    private final Function<T, Boolean> setter;

    public static DedicatedServerConfigEntry<Boolean> of(String id, boolean defaultValue, Function<DedicatedServer, Boolean> getter, BiConsumer<DedicatedServer, Boolean> setter) {
        return new DedicatedServerConfigEntry<>(id, EntryType.BOOLEAN, defaultValue, getter, setter);
    }

    public static DedicatedServerConfigEntry<Integer> of(String id, int defaultValue, Function<DedicatedServer, Integer> getter, BiConsumer<DedicatedServer, Integer> setter) {
        return new DedicatedServerConfigEntry<>(id, EntryType.INTEGER, defaultValue, getter, setter);
    }

    public static DedicatedServerConfigEntry<String> of(String id, String defaultValue, Function<DedicatedServer, String> getter, BiConsumer<DedicatedServer, String> setter) {
        return new DedicatedServerConfigEntry<>(id, EntryType.STRING, defaultValue, getter, setter);
    }

    private DedicatedServerConfigEntry(String id, EntryType type, T defaultValue, Function<DedicatedServer, T> getter, BiConsumer<DedicatedServer, T> setter) {
        this.type = type;
        this.options = EntryData.builder()
                .translation(id, "rconfig.server.properties." + id)
                .comment("", "rconfig.server.properties." + id + ".desc")
                .build();
        this.defaultValue = defaultValue;

        this.getter = () -> {
            var server = DedicatedServerInfo.getServer();
            return server == null ? this.defaultValue : getter.apply(server);
        };
        this.setter = newValue -> {
            var server = DedicatedServerInfo.getServer();
            if (server == null) return false;
            setter.accept(server, newValue);
            return true;
        };

        if (type == EntryType.ENUM || EntryType.OBJECT == type) {
            throw new IllegalArgumentException("EntryType cannot be ENUM or OBJECT");
        }
    }

    @Override
    public Object defaultValue() {
        return this.defaultValue;
    }

    @Override
    public Class<?> objectType() {
        return switch (type) {
            case BOOLEAN -> boolean.class;
            case BYTE -> byte.class;
            case SHORT -> short.class;
            case INTEGER -> int.class;
            case LONG -> long.class;
            case FLOAT -> float.class;
            case DOUBLE -> double.class;
            case STRING -> String.class;
            default -> null;
        };
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public Object get() {
        return this.getter.get();
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
        return 0;
    }

    @Override
    public boolean setByte(byte value) {
        return false;
    }

    @Override
    public short getShort() {
        return 0;
    }

    @Override
    public boolean setShort(short value) {
        return false;
    }

    @Override
    public int getInt() {
        if (this.type != EntryType.INTEGER) return 0;
        try {
            return (int) this.get();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setInt(int value) {
        if (this.type != EntryType.INTEGER) return false;
        return this.setter.apply((T) Integer.valueOf(value));
    }

    @Override
    public long getLong() {
        return 0;
    }

    @Override
    public boolean setLong(long value) {
        return false;
    }

    @Override
    public float getFloat() {
        return 0;
    }

    @Override
    public boolean setFloat(float value) {
        return false;
    }

    @Override
    public double getDouble() {
        return 0;
    }

    @Override
    public boolean setDouble(double value) {
        return false;
    }

    @Override
    public boolean getBoolean() {
        if (this.type != EntryType.BOOLEAN) return false;
        return (boolean) this.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setBoolean(boolean value) {
        if (this.type != EntryType.BOOLEAN) return false;
        return this.setter.apply((T) Boolean.valueOf(value));
    }

    @Override
    public String getString() {
        if (this.type != EntryType.STRING) return null;
        return (String) this.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean setString(String value) {
        if (this.type != EntryType.STRING) return false;
        return this.setter.apply((T) value);
    }

    @Override
    public Enum<?> getEnum() {
        return null;
    }

    @Override
    public boolean setEnum(Enum<?> value) {
        return false;
    }

    @Override
    public EntryType type() {
        return this.type;
    }

    @Override
    public EntryData options() {
        return this.options;
    }

    @Override
    public void reset() {
        this.setter.apply(this.defaultValue);
    }
}
