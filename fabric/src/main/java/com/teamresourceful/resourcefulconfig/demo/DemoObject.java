package com.teamresourceful.resourcefulconfig.demo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigObject;
import com.teamresourceful.resourcefulconfig.api.types.entries.SerializableObject;

@ConfigObject
public class DemoObject implements SerializableObject {

    @ConfigEntry(
        id = "string",
        translation = "Hello World!"
    )
    public String oldString = "Hello World!";

    @ConfigEntry(
        id = "integer",
        translation = "1"
    )
    public int oldInteger = 1;

    @Override
    public JsonElement save() {
        return new JsonPrimitive("%s:%d".formatted(oldString, oldInteger));
    }

    @Override
    public void load(JsonElement json) {
        if (json.isJsonPrimitive()) {
            String[] parts = json.getAsString().split(":");
            if (parts.length == 2) {
                this.oldString = parts[0];
                try {
                    this.oldInteger = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    this.oldInteger = 1; // Default value if parsing fails
                }
            }
        } else if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            this.oldString = jsonObject.get("string").getAsString();
            this.oldInteger = jsonObject.get("integer").getAsInt();
        }
    }
}
