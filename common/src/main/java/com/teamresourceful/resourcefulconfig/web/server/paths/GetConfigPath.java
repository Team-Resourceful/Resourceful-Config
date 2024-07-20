package com.teamresourceful.resourcefulconfig.web.server.paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.teamresourceful.resourcefulconfig.api.annotations.ConfigOption;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink;
import com.teamresourceful.resourcefulconfig.api.types.options.Option;
import com.teamresourceful.resourcefulconfig.api.types.options.EntryData;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;
import com.teamresourceful.resourcefulconfig.web.utils.WebServerUtils;
import com.teamresourceful.resourcefulconfig.web.utils.WebVerifier;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.function.Function;

@ApiStatus.Internal
public record GetConfigPath(WebVerifier verifier) implements BasePath {

    @Override
    public void handleCall(HttpExchange exchange, UserJwtPayload payload) throws IOException {
        String query = WebServerUtils.getQueryValue(exchange, "id");
        if (query != null) {
            ResourcefulConfig config = Configurations.INSTANCE.configs().get(query);
            if (config != null && !config.info().isHidden()) {
                JsonObject json = createWebConfig(config);
                WebServerUtils.send(exchange, HttpURLConnection.HTTP_OK, "application/json", json.toString().getBytes());
            } else {
                WebServerUtils.send(exchange, HttpURLConnection.HTTP_BAD_REQUEST, null, new byte[0]);
            }
        } else {
            WebServerUtils.send(exchange, HttpURLConnection.HTTP_BAD_REQUEST, null, new byte[0]);
        }
    }

    @Override
    public String method() {
        return "GET";
    }

    private static JsonObject createWebConfig(ResourcefulConfig config) {
        try {
            final ResourcefulConfigInfo info = config.info();
            JsonObject json = createWebConfigData(config);
            json.addProperty("icon", info.icon());
            json.addProperty("title", info.title().toLocalizedString());
            json.addProperty("description", info.description().toLocalizedString());
            JsonArray links = new JsonArray();
            for (ResourcefulConfigLink link : info.links()) {
                links.add(link.toJson());
            }
            json.add("links", links);
            return json;
        }catch (Exception e) {
            throw new RuntimeException("Failed to create config json for config: " + config.id(), e);
        }
    }

    private static JsonObject createWebConfigData(ResourcefulConfig config) {
        JsonObject json = new JsonObject();
        json.add("entries", createEntries(config));
        JsonArray categories = new JsonArray();
        config.categories().forEach((id, category) -> {
            ResourcefulConfigInfo info = category.info();
            if (!info.isHidden()) {
                JsonObject categoryJson = createWebConfigData(category);
                categoryJson.addProperty("id", id);
                categoryJson.addProperty("icon", info.icon());
                categoryJson.addProperty("title", info.title().toLocalizedString());
                categories.add(categoryJson);
            }
        });
        json.add("categories", categories);
        return json;
    }

    private static JsonArray createEntries(ResourcefulConfig config) {
        JsonArray array = new JsonArray();
        config.entries().forEach((id, entry) -> {
            JsonObject json = new JsonObject();
            if (!(entry instanceof ResourcefulConfigValueEntry valueEntry)) return;
            if (valueEntry.isArray()) return;
            switch (entry.type()) {
                case BYTE -> createNumber(json, valueEntry, ResourcefulConfigValueEntry::getByte);
                case SHORT -> createNumber(json, valueEntry, ResourcefulConfigValueEntry::getShort);
                case INTEGER -> createNumber(json, valueEntry, ResourcefulConfigValueEntry::getInt);
                case LONG -> createNumber(json, valueEntry, ResourcefulConfigValueEntry::getLong);
                case FLOAT -> createNumber(json, valueEntry, ResourcefulConfigValueEntry::getFloat);
                case DOUBLE -> createNumber(json, valueEntry, ResourcefulConfigValueEntry::getDouble);
                case ENUM -> {
                    Enum<?> def = (Enum<?>) valueEntry.defaultValue();
                    json.addProperty("type", "dropdown");
                    json.addProperty("current", valueEntry.getEnum().name());
                    json.addProperty("default", def.name());
                    JsonArray options = new JsonArray();
                    for (var e : getEnumConstants(valueEntry.objectType())) {
                        options.add(e.name());
                    }
                    json.add("options", options);
                }
                case BOOLEAN -> {
                    json.addProperty("type", "toggle");
                    json.addProperty("current", valueEntry.getBoolean());
                    json.addProperty("default", valueEntry.defaultOrElse(false));
                }
                case STRING -> {
                    json.addProperty("type", valueEntry.options().hasOption(Option.MULTILINE) ? "large-textbox" : "small-textbox");
                    json.addProperty("current", valueEntry.getString());
                    json.addProperty("default", valueEntry.defaultOrElse(""));
                }
            }
            json.addProperty("id", id);
            json.addProperty("title", valueEntry.options().title().toLocalizedString());
            json.addProperty("description", valueEntry.options().comment().toLocalizedString());
            array.add(json);
        });
        return array;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> void createNumber(JsonObject json, ResourcefulConfigValueEntry entry, Function<ResourcefulConfigValueEntry, T> getter) {
        final EntryData options = entry.options();
        T def = entry.defaultOrElse((T) WebServerUtils.ZERO);
        T current = getter.apply(entry);

        json.addProperty("type", options.hasOption(Option.RANGE) ? "range" : "number");
        json.addProperty("decimals", def instanceof Float || def instanceof Double);
        json.addProperty("current", current);
        json.addProperty("default", def);
        if (options.hasOption(Option.RANGE)) {
            ConfigOption.Range range = options.getOption(Option.RANGE);
            json.addProperty("min", range.min());
            json.addProperty("max", range.max());
            json.addProperty("step", 1);
        }
    }

    private static Enum<?>[] getEnumConstants(Class<?> clazz) {
        return (Enum<?>[]) clazz.getEnumConstants();
    }

}
