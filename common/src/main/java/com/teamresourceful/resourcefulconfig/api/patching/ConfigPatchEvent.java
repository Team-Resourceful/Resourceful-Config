package com.teamresourceful.resourcefulconfig.api.patching;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public interface ConfigPatchEvent {

    void register(int version, UnaryOperator<JsonObject> json);

    default void move(int version, String from, String to) {
        register(version, json -> {
            JsonObject object = getParent(json, from, false);
            if (object == null) return json;
            JsonObject parent = getParent(json, to, true);
            if (parent == null) return json;
            JsonElement element = object.remove(from.substring(from.lastIndexOf('.') + 1));
            parent.add(from.substring(from.lastIndexOf('.') + 1), element);
            return json;
        });
    }

    private static JsonObject getParent(JsonObject json, String path, boolean create) {
        List<String> parts = Arrays.asList(path.split("\\."));
        if (parts.size() == 1) return json;
        parts.removeLast();

        JsonElement element = json;
        for (String part : parts) {
            if (element instanceof JsonObject object) {
                if (object.has(part)) {
                    element = object.get(part);
                } else if (create) {
                    JsonObject newObject = new JsonObject();
                    object.add(part, newObject);
                    element = newObject;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return element instanceof JsonObject object ? object : null;
    }
}
