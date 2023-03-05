package com.teamresourceful.resourcefulconfig.web.info;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.teamresourceful.resourcefulconfig.web.annotations.Link;
import com.teamresourceful.resourcefulconfig.web.annotations.WebInfo;

public record ResourcefulWebConfig(boolean hidden, String icon, String color, String title, String description, Link[] links) {

    public static final ResourcefulWebConfig DEFAULT = new ResourcefulWebConfig(true, "box", "#ffffff", "", "", new Link[0]);

    public static ResourcefulWebConfig of(Class<?> clazz) {
        WebInfo config = clazz.getAnnotation(WebInfo.class);
        if (config == null) return DEFAULT;
        return new ResourcefulWebConfig(config.hidden(), config.icon(), config.color(), config.title(), config.description(), config.links());
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
