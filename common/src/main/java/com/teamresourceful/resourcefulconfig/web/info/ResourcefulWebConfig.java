package com.teamresourceful.resourcefulconfig.web.info;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.teamresourceful.resourcefulconfig.web.annotations.Gradient;
import com.teamresourceful.resourcefulconfig.web.annotations.Link;
import com.teamresourceful.resourcefulconfig.web.annotations.WebInfo;

public record ResourcefulWebConfig(boolean hidden, String icon, String color, Gradient gradient, String title, String description, Link[] links) {

    public static final ResourcefulWebConfig DEFAULT = new ResourcefulWebConfig(true, "box", "#ffffff", null, "", "", new Link[0]);
    public static final ResourcefulWebConfig NO_HIDE = new ResourcefulWebConfig(false, "box", "#ffffff", null, "", "", new Link[0]);

    public static ResourcefulWebConfig of(Class<?> clazz) {
        WebInfo config = clazz.getAnnotation(WebInfo.class);
        if (config == null) return DEFAULT;
        return new ResourcefulWebConfig(config.hidden(), config.icon(), config.color(), config.gradient(), config.title(), config.description(), config.links());
    }

    public static ResourcefulWebConfig showOf(ResourcefulWebConfig config) {
         if (config == DEFAULT) return NO_HIDE;
         return config;
     }

    public JsonElement toColor() {
        if (gradient().disabled()) {
            return new JsonPrimitive(color());
        } else {
            JsonObject json = new JsonObject();
            json.addProperty("degree", gradient().value());
            json.addProperty("first", gradient().first());
            json.addProperty("second", gradient().second());
            return json;
        }
    }

    public JsonArray toJsonLinks() {
        JsonArray links = new JsonArray();
        for (Link link : links()) {
            JsonObject json = new JsonObject();
            json.addProperty("title", link.title());
            json.addProperty("icon", link.icon());
            json.addProperty("url", link.value());
            links.add(json);
        }
        return links;
    }
}
