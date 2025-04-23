package com.teamresourceful.resourcefulconfig.web.server.paths;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfigElement;
import com.teamresourceful.resourcefulconfig.api.types.elements.ResourcefulConfigEntryElement;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigEntry;
import com.teamresourceful.resourcefulconfig.api.types.entries.ResourcefulConfigValueEntry;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;
import com.teamresourceful.resourcefulconfig.web.utils.WebServerUtils;
import com.teamresourceful.resourcefulconfig.web.utils.WebVerifier;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

@ApiStatus.Internal
public record PostSavePath(WebVerifier verifier) implements BasePath {

    @Override
    public void handleCall(HttpExchange exchange, UserJwtPayload payload) throws IOException {
        String query = WebServerUtils.getQueryValue(exchange, "id");
        if (query != null) {
            ResourcefulConfig config = Configurations.INSTANCE.configs().get(query);
            if (config != null && !config.info().isHidden()) {
                try {
                    String data = IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    JsonObject object = WebServerUtils.GSON.fromJson(data, JsonObject.class);
                    saveConfig(config, object);
                    WebServerUtils.send(exchange, HttpURLConnection.HTTP_OK, null, new byte[0]);
                } catch (Throwable e) {
                    WebServerUtils.send(exchange, HttpURLConnection.HTTP_BAD_REQUEST, null, new byte[0]);
                }
            } else {
                WebServerUtils.send(exchange, HttpURLConnection.HTTP_BAD_REQUEST, null, new byte[0]);
            }
        } else {
            WebServerUtils.send(exchange, HttpURLConnection.HTTP_BAD_REQUEST, null, new byte[0]);
        }
    }

    @Override
    public String method() {
        return "POST";
    }

    private static void saveConfig(ResourcefulConfig config, JsonObject object) {
        object.asMap().forEach((key, value) -> saveEntry(config, key, value));
        config.save();
    }

    private static void saveEntry(ResourcefulConfig config, String key, JsonElement json) {
        LinkedHashMap<String, ResourcefulConfigEntry> entries = new LinkedHashMap<>();
        for (ResourcefulConfigElement element : config.elements()) {
            if (element instanceof ResourcefulConfigEntryElement entry) {
                entries.put(entry.id(), entry.entry());
            }
        }

        if (key.contains(";")) {
            String[] split = key.split(";", 2);
            if (split.length == 2) {
                String id = split[0];
                ResourcefulConfig category = config.categories().get(id);
                if (category != null) {
                    saveEntry(category, split[1], json);
                }
            }
        } else if (key.contains(":")) {
            String[] split = key.split(":", 2);
            if (split.length == 2) {
                String id = split[0];
                ResourcefulConfig category = config.categories().get(id);
                if (category != null) {
                    saveEntry(category, split[1], json);
                }
            }
        } else {
            ResourcefulConfigEntry entry = entries.get(key);
            if (!(entry instanceof ResourcefulConfigValueEntry valueEntry)) return;
            switch (entry.type()) {
                case BOOLEAN -> valueEntry.setBoolean(json.getAsBoolean());
                case BYTE -> valueEntry.setByte(json.getAsByte());
                case SHORT -> valueEntry.setShort(json.getAsShort());
                case INTEGER -> valueEntry.setInt(json.getAsInt());
                case LONG -> valueEntry.setLong(json.getAsLong());
                case FLOAT -> valueEntry.setFloat(json.getAsFloat());
                case DOUBLE -> valueEntry.setDouble(json.getAsDouble());
                case STRING -> valueEntry.setString(json.getAsString());
                case ENUM -> valueEntry.setEnum(ParsingUtils.parseEnum(valueEntry.objectType(), json.getAsString()));
            }
        }
    }
}
