package com.teamresourceful.resourcefulconfig.web.server.paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.teamresourceful.resourcefulconfig.api.types.ResourcefulConfig;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigInfo;
import com.teamresourceful.resourcefulconfig.api.types.info.ResourcefulConfigLink;
import com.teamresourceful.resourcefulconfig.common.config.Configurations;
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
            if (!config.info().isHidden()) {
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
        try {
            final ResourcefulConfigInfo info = config.info();
            JsonObject json = new JsonObject();
            json.addProperty("id", config.id());
            json.addProperty("title", info.title().toLocalizedString());
            json.addProperty("description", info.description().toLocalizedString());
            json.addProperty("icon", info.icon());
            json.add("color", info.color().toJson());
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
}
