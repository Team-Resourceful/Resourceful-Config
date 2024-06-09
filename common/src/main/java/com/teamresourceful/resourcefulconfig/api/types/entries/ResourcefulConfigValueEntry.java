package com.teamresourceful.resourcefulconfig.api.types.entries;

public interface ResourcefulConfigValueEntry extends ResourcefulConfigEntry {

    Object defaultValue();

    Class<?> objectType();

    @SuppressWarnings("unchecked")
    default <T> T defaultOrElse(T value) {
        final Object defaultValue = defaultValue();
        return defaultValue == null ? value : (T) defaultValue;
    }

    boolean isArray();

    Object get();

    Object[] getArray();

    boolean setArray(Object[] array);

    byte getByte();

    boolean setByte(byte value);

    short getShort();

    boolean setShort(short value);

    int getInt();

    boolean setInt(int value);

    long getLong();

    boolean setLong(long value);

    float getFloat();

    boolean setFloat(float value);

    double getDouble();

    boolean setDouble(double value);

    boolean getBoolean();

    boolean setBoolean(boolean value);

    String getString();

    boolean setString(String value);

    Enum<?> getEnum();

    boolean setEnum(Enum<?> value);

}
