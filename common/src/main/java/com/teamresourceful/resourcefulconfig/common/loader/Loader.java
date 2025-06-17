package com.teamresourceful.resourcefulconfig.common.loader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigObjectEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.SerializableObject;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryType;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

public class Loader {

    public static void loadConfig(ResourcefulConfig config, JsonObject json) {
        forEach(config.elements(), (id, entry) -> {
            JsonElement data = json.get(id);
            if (data == null) return;
            if (entry instanceof ResourcefulConfigObjectEntry objectEntry) {
                if (objectEntry.instance() instanceof SerializableObject serializable) {
                    serializable.load(data);
                } else if (data instanceof JsonObject object) {
                    loadObject(objectEntry, object);
                }
            } else if (entry instanceof ResourcefulConfigValueEntry value) {
                if (!setValue(data, id, value)) {
                    ModUtils.log("Failed to set value for " + id);
                }
            }
        });

        config.categories().forEach((id, category) -> {
            JsonElement data = json.get(id);
            if (!(data instanceof JsonObject object)) return;
            loadConfig(category, object);
        });
    }

    private static void loadObject(ResourcefulConfigObjectEntry object, JsonObject json) {
        forEach(object.elements(), (id, entry) -> {
            if (entry instanceof ResourcefulConfigValueEntry value) {
                JsonElement data = json.get(id);
                if (data == null || data instanceof JsonObject) return;
                if (!setValue(data, id, value)) {
                    ModUtils.log("Failed to set value for " + id);
                }
            }
        });
    }

    private static Object convert(JsonElement element, String id, ResourcefulConfigValueEntry entry) {
        if (element instanceof JsonArray array) {
            List<Object> list = new ArrayList<>();
            array.forEach(e -> list.add(convert(e, id, entry)));
            list.removeIf(Objects::isNull);
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
                    Enum<?> value = ParsingUtils.parseEnum(entry.objectType(), primitive.getAsString());
                    if (value != null) {
                        return value;
                    }
                    ModUtils.log("Failed to parse enum value for " + id);
                }
                return primitive.getAsString();
            }
        }
        return null;
    }


    private static boolean setValue(JsonElement json, String id, ResourcefulConfigValueEntry data) {
        Object o = convert(json, id, data);
        if (o instanceof List<?> list) return data.setArray(list.toArray());
        else if (o instanceof String string) return data.setString(string);
        else if (o instanceof Boolean booleanValue) return data.setBoolean(booleanValue);
        else if (o instanceof Short shortValue) return data.setShort(shortValue);
        else if (o instanceof Byte byteValue) return data.setByte(byteValue);
        else if (o instanceof Float floatValue) return data.setFloat(floatValue);
        else if (o instanceof Integer integerValue) return data.setInt(integerValue);
        else if (o instanceof Long longValue) return data.setLong(longValue);
        else if (o instanceof Double doubleValue) return data.setDouble(doubleValue);
        else if (o instanceof Enum<?> enumValue) return data.setEnum(enumValue);
        return true;
    }

    private static void forEach(List<ResourcefulConfigElement> elements, BiConsumer<String, ResourcefulConfigEntry> consumer) {
        for (ResourcefulConfigElement element : elements) {
            if (element instanceof ResourcefulConfigEntryElement entry) {
                consumer.accept(entry.id(), entry.entry());
            }
        }
    }
}
