package com.teamresourceful.resourcefulconfig.web.server.paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.teamresourceful.resourcefulconfig.common.annotations.*;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.web.annotations.Multiline;
import com.teamresourceful.resourcefulconfig.web.info.ResourcefulWebConfig;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;
import com.teamresourceful.resourcefulconfig.web.utils.WebServerUtils;
import com.teamresourceful.resourcefulconfig.web.utils.WebVerifier;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.util.function.BiFunction;

@ApiStatus.Internal
public record GetConfigPath(WebVerifier verifier) implements BasePath {

    @Override
    public void handleCall(HttpExchange exchange, UserJwtPayload payload) throws IOException {
        String query = WebServerUtils.getQueryValue(exchange, "id");
        if (query != null) {
            ResourcefulConfig config = Configurations.INSTANCE.configs().get(query);
            if (config != null && !config.getWebConfig().hidden()) {
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
        final ResourcefulWebConfig resourcefulWebConfig = config.getWebConfig();
        JsonObject json = createWebConfigData(config);
        json.addProperty("icon", resourcefulWebConfig.icon());
        json.addProperty("title", resourcefulWebConfig.title());
        json.addProperty("description", resourcefulWebConfig.description());
        json.add("links", resourcefulWebConfig.toJsonLinks());
        return json;
    }

    private static JsonObject createWebConfigData(ResourcefulConfig config) {
        JsonObject json = new JsonObject();
        json.add("entries", createEntries(config));
        JsonArray categories = new JsonArray();
        config.getSubConfigs().forEach((id, category) -> {
            ResourcefulWebConfig info = ResourcefulWebConfig.showOf(category.getClass());
            if (!info.hidden()) {
                JsonObject categoryJson = createWebConfigData(category);
                categoryJson.addProperty("id", id);
                categoryJson.addProperty("icon", info.icon());
                categoryJson.addProperty("title", getTitle(info.title(), category));
                categories.add(categoryJson);
            }
        });
        json.add("categories", categories);
        return json;
    }

    private static JsonArray createEntries(ResourcefulConfig config) {
        JsonArray array = new JsonArray();
        config.getEntries().forEach((id, entry) -> {
            JsonObject json = new JsonObject();
            switch (entry.type()) {
                case BYTE -> {
                    ByteRange byteRange = entry.getAnnotation(ByteRange.class);
                    NumberRange range = byteRange != null ? new NumberRange(byteRange.min(), byteRange.max()) : null;
                    createNumber(json, entry, range, ParsingUtils::getByte);
                }
                case SHORT -> {
                    ShortRange shortRange = entry.getAnnotation(ShortRange.class);
                    NumberRange range = shortRange != null ? new NumberRange(shortRange.min(), shortRange.max()) : null;
                    createNumber(json, entry,range, ParsingUtils::getShort);
                }
                case INTEGER -> {
                    IntRange intRange = entry.getAnnotation(IntRange.class);
                    NumberRange range = intRange != null ? new NumberRange(intRange.min(), intRange.max()) : null;
                    createNumber(json, entry, range, ParsingUtils::getInt);
                }
                case LONG -> {
                    LongRange longRange = entry.getAnnotation(LongRange.class);
                    NumberRange range = longRange != null ? new NumberRange(longRange.min(), longRange.max()) : null;
                    createNumber(json, entry, range, ParsingUtils::getLong);
                }
                case FLOAT -> {
                    FloatRange floatRange = entry.getAnnotation(FloatRange.class);
                    NumberRange range = floatRange != null ? new NumberRange(floatRange.min(), floatRange.max()) : null;
                    createNumber(json, entry, range, ParsingUtils::getFloat);
                }
                case DOUBLE -> {
                    DoubleRange doubleRange = entry.getAnnotation(DoubleRange.class);
                    NumberRange range = doubleRange != null ? new NumberRange(doubleRange.min(), doubleRange.max()) : null;
                    createNumber(json, entry, range, ParsingUtils::getDouble);
                }
                case ENUM -> {
                    Enum<?> def = (Enum<?>) entry.defaultValue();
                    json.addProperty("type", "dropdown");
                    json.addProperty("current", ParsingUtils.getEnum(entry.field(), def).name());
                    json.addProperty("default", def.name());
                    JsonArray options = new JsonArray();
                    for (var e : getEnumConstants(entry.field().getType())) {
                        options.add(e.name());
                    }
                    json.add("options", options);
                }
                case BOOLEAN -> {
                    boolean def = entry.getDefaultOrElse(false);
                    json.addProperty("type", "toggle");
                    json.addProperty("current", ParsingUtils.getBoolean(entry.field(), def));
                    json.addProperty("default", def);
                }
                case STRING -> {
                    boolean multiline = entry.getAnnotation(Multiline.class) != null;
                    String def = entry.getDefaultOrElse("");
                    json.addProperty("type", multiline ? "large-textbox" : "small-textbox");
                    json.addProperty("current", ParsingUtils.getString(entry.field(), def));
                    json.addProperty("default", def);
                }
            }
            json.addProperty("id", id);
            json.addProperty("title", getTitle(entry, id));
            json.addProperty("description", getDescription(entry));
            array.add(json);
        });
        return array;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> void createNumber(JsonObject json, ResourcefulConfigEntry entry, NumberRange range, BiFunction<Field, T, T> getter) {
        T def = entry.getDefaultOrElse((T)WebServerUtils.ZERO);
        T current = getter.apply(entry.field(), def);

        json.addProperty("type",  range != null ? "range" : "number");
        json.addProperty("decimals", def instanceof Float || def instanceof Double);
        json.addProperty("current", current);
        json.addProperty("default", def);
        if (range != null) {
            json.addProperty("min", range.min());
            json.addProperty("max", range.max());
            json.addProperty("step", 1);
        }
    }

    private static Enum<?>[] getEnumConstants(Class<?> clazz) {
        return (Enum<?>[]) clazz.getEnumConstants();
    }

    private static String getTitle(ResourcefulConfigEntry entry, String def) {
        ConfigEntry configEntry = entry.getAnnotation(ConfigEntry.class);
        if (configEntry != null) {
            return I18n.get(configEntry.translation());
        }
        return def;
    }

    private static String getTitle(String input, ResourcefulConfig config) {
        return input.isBlank() ? config.getDisplayName().getString() : input;
    }

    private static String getDescription(ResourcefulConfigEntry entry) {
        Comment comment = entry.getAnnotation(Comment.class);
        if (comment != null) {
            return comment.value();
        }
        return "";
    }

    private record NumberRange(Number min, Number max) {}

}
