package com.teamresourceful.resourcefulconfig.web.utils;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.teamresourceful.resourcefulconfig.web.config.WebServerConfig;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;
import com.teamresourceful.resourcefulconfig.web.server.WebServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@ApiStatus.Internal
public final class WebVerifier {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final String origin;
    private final String authUrl;

    public WebVerifier(WebServerConfig config) {
        this.origin = config.getSite();
        this.authUrl = origin +"/api/v1/private/verify";
    }

    private UserJwtPayload verify(UserJwtPayload info) {
        return WebServer.verify(info) ? info : null;
    }

    @Nullable
    public UserJwtPayload getInfo(HttpExchange exchange) {
        return getInfo(exchange.getRequestHeaders().getFirst("Authorization"));
    }

    @Nullable
    public UserJwtPayload getInfo(String jwt) {
        if (jwt != null) {
            try {
                HttpRequest request = HttpRequest.newBuilder(new URI(this.authUrl))
                        .GET()
                        .version(HttpClient.Version.HTTP_2)
                        .header("Authorization", jwt)
                        .header("User-Agent", "ResourcefulConfig")
                        .build();

                final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                    final JsonObject body = WebServerUtils.GSON.fromJson(response.body(), JsonObject.class);
                    if (WebServerUtils.getBool(body, "valid") && body.has("user")) {
                        return verify(UserJwtPayload.fromJson(body.get("user")));
                    }
                }
                return null;
            } catch (Exception ignored) {
                return null;
            }
        }
        return null;
    }

    public String origin() {
        return this.origin;
    }
}