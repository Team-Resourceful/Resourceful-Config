package com.teamresourceful.resourcefulconfig.common.compat.minecraft;

import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import com.teamresourceful.resourcefulconfig.mixins.common.DedicatedServerAccessor;
import com.teamresourceful.resourcefulconfig.mixins.common.SettingsAccessor;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;

import java.util.Objects;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DedicatedServerEntry implements ResourcefulConfigValueEntry {

    private final EntryType type;
    private final EntryData options;

    private final String defaultValue;

    private final Supplier<String> getter;
    private final Function<String, Boolean> setter;

    public DedicatedServerEntry(String id, boolean defaultValue, BiConsumer<DedicatedServer, Boolean> onUpdate) {
        this(id, EntryType.BOOLEAN, String.valueOf(defaultValue), (server, value) -> onUpdate.accept(server, value.getBoolean()));
    }

    public DedicatedServerEntry(String id, int defaultValue, BiConsumer<DedicatedServer, Integer> onUpdate) {
        this(id, EntryType.INTEGER, String.valueOf(defaultValue), (server, value) -> onUpdate.accept(server, value.getInt()));
    }

    public DedicatedServerEntry(String id, String defaultValue, BiConsumer<DedicatedServer, String> onUpdate) {
        this(id, EntryType.STRING, defaultValue, (server, value) -> onUpdate.accept(server, value.getString()));
    }

    public DedicatedServerEntry(String id, EntryType entryType, String defaultValue, BiConsumer<DedicatedServer, DedicatedServerEntry> onUpdate) {
        this(
                id,
                entryType,
                EntryData.builder()
                        .translation(id, "rconfig.server.properties." + id)
                        .comment("", "rconfig.server.properties." + id + ".desc")
                        .build(),
                defaultValue,
                onUpdate
        );
    }

    public DedicatedServerEntry(String id, EntryType entryType, EntryData options, String defaultValue, BiConsumer<DedicatedServer, DedicatedServerEntry> onUpdate) {
        this.type = entryType;
        this.options = options;
        this.defaultValue = defaultValue;

        this.getter = () -> ((SettingsAccessor) Objects.requireNonNull(DedicatedServerInfo.getServer()).getProperties()).invokeGetStringRaw(id);
        this.setter = newValue -> {
            var server = DedicatedServerInfo.getServer();
            if (server == null) return false;
            ((DedicatedServerAccessor) server).getSettings().update(properties -> {
                Properties newProps = ((SettingsAccessor) server.getProperties()).invokeCloneProperties();
                newProps.setProperty(id, newValue);
                return new DedicatedServerProperties(newProps);
            });
            onUpdate.accept(server, this);
            return true;
        };

        if (entryType == EntryType.ENUM || EntryType.OBJECT == entryType) {
            throw new IllegalArgumentException("EntryType cannot be ENUM or OBJECT");
        }
    }

    @Override
    public Object defaultValue() {
        return switch (type) {
            case BOOLEAN -> Boolean.parseBoolean(defaultValue);
            case BYTE -> Byte.parseByte(defaultValue);
            case SHORT -> Short.parseShort(defaultValue);
            case INTEGER -> Integer.parseInt(defaultValue);
            case LONG -> Long.parseLong(defaultValue);
            case FLOAT -> Float.parseFloat(defaultValue);
            case DOUBLE -> Double.parseDouble(defaultValue);
            case STRING -> defaultValue;
            default -> null;
        };
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
        return switch (type) {
            case BOOLEAN -> Boolean.parseBoolean(getter.get());
            case BYTE -> Byte.parseByte(getter.get());
            case SHORT -> Short.parseShort(getter.get());
            case INTEGER -> Integer.parseInt(getter.get());
            case LONG -> Long.parseLong(getter.get());
            case FLOAT -> Float.parseFloat(getter.get());
            case DOUBLE -> Double.parseDouble(getter.get());
            case STRING -> getter.get();
            default -> null;
        };
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
        if (this.type != EntryType.BYTE) return 0;
        try {
            return Byte.parseByte(this.getter.get());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean setByte(byte value) {
        if (this.type != EntryType.BYTE) return false;
        return this.setter.apply(String.valueOf(value));
    }

    @Override
    public short getShort() {
        if (this.type != EntryType.SHORT) return 0;
        try {
            return Short.parseShort(this.getter.get());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean setShort(short value) {
        if (this.type != EntryType.SHORT) return false;
        return this.setter.apply(String.valueOf(value));
    }

    @Override
    public int getInt() {
        if (this.type != EntryType.INTEGER) return 0;
        try {
            return Integer.parseInt(this.getter.get());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean setInt(int value) {
        if (this.type != EntryType.INTEGER) return false;
        return this.setter.apply(String.valueOf(value));
    }

    @Override
    public long getLong() {
        if (this.type != EntryType.LONG) return 0;
        try {
            return Long.parseLong(this.getter.get());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean setLong(long value) {
        if (this.type != EntryType.LONG) return false;
        return this.setter.apply(String.valueOf(value));
    }

    @Override
    public float getFloat() {
        if (this.type != EntryType.FLOAT) return 0;
        try {
            return Float.parseFloat(this.getter.get());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean setFloat(float value) {
        if (this.type != EntryType.FLOAT) return false;
        return this.setter.apply(String.valueOf(value));
    }

    @Override
    public double getDouble() {
        if (this.type != EntryType.DOUBLE) return 0;
        try {
            return Double.parseDouble(this.getter.get());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public boolean setDouble(double value) {
        if (this.type != EntryType.DOUBLE) return false;
        return this.setter.apply(String.valueOf(value));
    }

    @Override
    public boolean getBoolean() {
        if (this.type != EntryType.BOOLEAN) return false;
        return Boolean.parseBoolean(this.getter.get());
    }

    @Override
    public boolean setBoolean(boolean value) {
        if (this.type != EntryType.BOOLEAN) return false;
        return this.setter.apply(String.valueOf(value));
    }

    @Override
    public String getString() {
        if (this.type != EntryType.STRING) return null;
        return this.getter.get();
    }

    @Override
    public boolean setString(String value) {
        if (this.type != EntryType.STRING) return false;
        return this.setter.apply(value);
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
