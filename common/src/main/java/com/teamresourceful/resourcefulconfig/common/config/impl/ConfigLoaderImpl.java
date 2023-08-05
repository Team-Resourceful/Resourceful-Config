package com.teamresourceful.resourcefulconfig.common.config.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.teamresourceful.resourcefulconfig.common.annotations.*;
import com.teamresourceful.resourcefulconfig.common.config.*;
import com.teamresourceful.resourcefulconfig.common.jsonc.JsoncArray;
import com.teamresourceful.resourcefulconfig.common.jsonc.JsoncElement;
import com.teamresourceful.resourcefulconfig.common.jsonc.JsoncObject;
import com.teamresourceful.resourcefulconfig.common.jsonc.JsoncPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ConfigLoaderImpl implements ConfigLoader {


    public ResourcefulConfig registerConfig(Class<?> configClass) {
        try {
            ResourcefulConfigImpl config = ConfigParser.parseConfig(configClass);
            config.load();
            config.save(); //We save after we load to clear out old values that are no longer in the config and to add new ones.
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to create config for " + configClass.getName());
        }
        return null;
    }

    public static JsoncObject saveConfig(ResourcefulConfig config, JsoncObject object) {
        for (var value : config.getEntries().entrySet()) {
            String id = value.getKey();
            ResourcefulConfigEntry entry = value.getValue();
            JsoncElement element = getElement(ParsingUtils.getField(entry.field()));
            if (element != null) {
                element.comment(getCommentAndInfo(entry));
                object.add(id, element);
            }
        }

        for (var value : config.getSubConfigs().entrySet()) {
            object.add(value.getKey(), saveConfig(value.getValue(), new JsoncObject()));
        }

        return object;
    }

    private static String getCommentAndInfo(ResourcefulConfigEntry entry) {
        StringBuilder builder = new StringBuilder();
        entry.getOptAnnotation(Comment.class).map(Comment::value).ifPresent(builder::append);
        List<String> rangeString = getInfoString(entry);
        if (!rangeString.isEmpty()) {
            if (builder.length() > 0) builder.append("\n");
            builder.append(String.join("\n", rangeString));
        }
        return builder.toString();
    }

    private static List<String> getInfoString(ResourcefulConfigEntry entry) {
        List<String> list = new ArrayList<>();
        entry.getOptAnnotation(ByteRange.class).ifPresent(range -> list.add("Range: " + range.min() + " - " + range.max()));
        entry.getOptAnnotation(ShortRange.class).ifPresent(range -> list.add("Range: " + range.min() + " - " + range.max()));
        entry.getOptAnnotation(IntRange.class).ifPresent(range -> list.add("Range: " + range.min() + " - " + range.max()));
        entry.getOptAnnotation(LongRange.class).ifPresent(range -> list.add("Range: " + range.min() + " - " + range.max()));
        entry.getOptAnnotation(FloatRange.class).ifPresent(range -> list.add("Range: " + range.min() + " - " + range.max()));
        entry.getOptAnnotation(DoubleRange.class).ifPresent(range -> list.add("Range: " + range.min() + " - " + range.max()));
        if (entry.type() == EntryType.ENUM) {
            List<String> enumNames = new ArrayList<>();
            for (Enum<?> enumConstant : ((Enum<?>[]) entry.field().getType().getEnumConstants())) {
                enumNames.add(enumConstant.name());
            }
            list.add("Valid Values: " + String.join(", ", enumNames));
        }
        if (list.isEmpty() && entry.type() != EntryType.BOOLEAN && entry.type() != EntryType.STRING) {
            list.add("Type: " + entry.type().name().charAt(0) + entry.type().name().substring(1).toLowerCase());
        }
        return list;
    }

    private static JsoncElement getElement(Object value) {
        if (value == null) throw new NullPointerException("Config value cannot be null!");
        if (value instanceof String string) return new JsoncPrimitive(string);
        if (value instanceof Number number) return new JsoncPrimitive(number);
        if (value instanceof Boolean bool) return new JsoncPrimitive(bool);
        if (value instanceof Enum<?> enumValue) return new JsoncPrimitive(enumValue.name());
        if (value.getClass().isArray()) {
            JsoncArray array = new JsoncArray();
            for (Object o : ParsingUtils.forceObjectArray(value)) {
                array.add(getElement(o));
            }
            return array;
        }
        return null;
    }

    public static void loadConfig(ResourcefulConfig config, JsonObject json) {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().startsWith("#")) continue; //Legacy Comment
            if (entry.getValue() instanceof JsonObject subConfig) {
                config.getSubConfig(entry.getKey()).ifPresent(cat -> loadConfig(cat, subConfig));
            } else {
                config.getEntry(entry.getKey()).ifPresent(data -> {
                    if (!setValue(entry.getValue(), data)) {
                        System.out.println("Failed to set value for " + entry.getKey());
                    }
                });
            }
        }
    }

    private static Object convert(JsonElement element, ResourcefulConfigEntry entry) {
        if (element instanceof JsonArray array) {
            List<Object> list = new ArrayList<>();
            array.forEach(e -> list.add(convert(e, entry)));
            return list;
        } else if (element instanceof JsonPrimitive primitive) {
            if (primitive.isBoolean()) {
                return primitive.getAsBoolean();
            } else if (primitive.isNumber()) {
                Number number = primitive.getAsNumber();
                return switch (entry.type()) {
                    case BYTE -> number.byteValue();
                    case SHORT -> number.shortValue();
                    case INTEGER -> number.intValue();
                    case LONG -> number.longValue();
                    case FLOAT -> number.floatValue();
                    case DOUBLE -> number.doubleValue();
                    default -> number;
                };
            } else if (primitive.isString()) {
                if (entry.type() == EntryType.ENUM) {
                    Enum<?> value = ParsingUtils.getEnum(entry.field().getType(), primitive.getAsString());
                    if (value != null) {
                        return value;
                    }
                    throw new IllegalArgumentException("Invalid enum value for " + entry.field().getName());
                }
                return primitive.getAsString();
            }
        }
        return null;
    }


    private static boolean setValue(JsonElement json, ResourcefulConfigEntry data) {
        Object o = convert(json, data);
        if (o instanceof List<?> list) {
            return data.setArray(list.toArray());
        } else if (o instanceof String string) {
            return data.setString(string);
        } else if (o instanceof Boolean booleanValue) {
            return data.setBoolean(booleanValue);
        } else if (o instanceof Short shortValue) {
            return data.setShort(shortValue);
        } else if (o instanceof Byte byteValue) {
            return data.setByte(byteValue);
        } else if (o instanceof Float floatValue) {
            return data.setFloat(floatValue);
        } else if (o instanceof Integer integerValue) {
            return data.setInt(integerValue);
        } else if (o instanceof Long longValue) {
            return data.setLong(longValue);
        } else if (o instanceof Double doubleValue) {
            return data.setDouble(doubleValue);
        } else if (o instanceof Enum<?> enumValue) {
            return data.setEnum(enumValue);
        }
        return true;
    }
}
