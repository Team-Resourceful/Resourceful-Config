package com.teamresourceful.resourcefulconfig.web.server.paths;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import com.teamresourceful.resourcefulconfig.common.config.ParsingUtils;
import com.teamresourceful.resourcefulconfig.common.config.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;
import com.teamresourceful.resourcefulconfig.web.utils.WebServerUtils;
import com.teamresourceful.resourcefulconfig.web.utils.WebVerifier;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

@ApiStatus.Internal
public record PostSavePath(WebVerifier verifier) implements BasePath {

    @Override
    public void handleCall(HttpExchange exchange, UserJwtPayload payload) throws IOException {
        String query = WebServerUtils.getQueryValue(exchange, "id");
        if (query != null) {
            ResourcefulConfig config = Configurations.INSTANCE.configs().get(query);
            if (config != null && !config.getWebConfig().hidden()) {
                try {
                    String data = IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    JsonObject object = WebServerUtils.GSON.fromJson(data, JsonObject.class);
                    saveConfig(config, object);
                    WebServerUtils.send(exchange, HttpURLConnection.HTTP_OK, null, new byte[0]);
                } catch (Exception e) {
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

    private static void saveEntry(ResourcefulConfig config, String key, JsonElement element) {
        if (key.contains(";")) {
            String[] split = key.split(";", 2);
            if (split.length == 2) {
                String id = split[0];
                config.getSubConfig(id)
                    .ifPresent(subConfig -> saveEntry(subConfig, split[1], element));
            }
        } else if (key.contains(":")) {
            String[] split = key.split(":", 2);
            if (split.length == 2) {
                String id = split[0];
                config.getSubConfig(id)
                    .ifPresent(subConfig -> saveEntry(subConfig, split[1], element));
            }
        } else {
            config.getEntry(key)
                .ifPresent(entry -> {
                    switch (entry.type()) {
                        case BOOLEAN -> entry.setBoolean(element.getAsBoolean());
                        case BYTE -> entry.setByte(element.getAsByte());
                        case SHORT -> entry.setShort(element.getAsShort());
                        case INTEGER -> entry.setInt(element.getAsInt());
                        case LONG -> entry.setLong(element.getAsLong());
                        case FLOAT -> entry.setFloat(element.getAsFloat());
                        case DOUBLE -> entry.setDouble(element.getAsDouble());
                        case STRING -> entry.setString(element.getAsString());
                        case ENUM -> entry.setEnum(ParsingUtils.getEnum(entry.field().getType(), element.getAsString()));
                    }
                });
        }
    }
}
