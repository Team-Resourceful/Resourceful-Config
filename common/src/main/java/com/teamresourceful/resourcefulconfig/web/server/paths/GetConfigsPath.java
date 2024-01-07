package com.teamresourceful.resourcefulconfig.web.server.paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
import com.teamresourceful.resourcefulconfig.web.info.ResourcefulWebConfig;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;
import com.teamresourceful.resourcefulconfig.web.utils.WebServerUtils;
import com.teamresourceful.resourcefulconfig.web.utils.WebVerifier;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.HttpURLConnection;

@ApiStatus.Internal
public record GetConfigsPath(WebVerifier verifier) implements BasePath {

    @Override
    public void handleCall(HttpExchange exchange, UserJwtPayload payload) throws IOException {
        JsonArray array = new JsonArray();
        for (ResourcefulConfig config : Configurations.INSTANCE) {
            if (!config.webConfig().hidden()) {
                array.add(createConfigJson(config));
            }
        }
        final byte[] data = array.toString().getBytes();
        WebServerUtils.send(exchange, HttpURLConnection.HTTP_OK, "application/json", data);
    }

    @Override
    public String method() {
        return "GET";
    }

    private static JsonObject createConfigJson(ResourcefulConfig config) {
        final ResourcefulWebConfig resourcefulWebConfig = config.webConfig();
        JsonObject json = new JsonObject();
        json.addProperty("id", config.id());
        json.addProperty("title", resourcefulWebConfig.title());
        json.addProperty("description", resourcefulWebConfig.description());
        json.addProperty("icon", resourcefulWebConfig.icon());
        json.add("color", resourcefulWebConfig.toColor());
        json.add("links", resourcefulWebConfig.toJsonLinks());
        return json;
    }
}
