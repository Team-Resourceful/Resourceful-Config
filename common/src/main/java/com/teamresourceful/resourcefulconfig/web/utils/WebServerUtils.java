package com.teamresourceful.resourcefulconfig.web.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;

@ApiStatus.Internal
public final class WebServerUtils {

    public static final Number ZERO = 0;

    public static final Gson GSON = new Gson();
    public static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();

    private WebServerUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void send(@NotNull HttpExchange exchange, int code, @Nullable String contentType, byte[] bytes) throws IOException {
        if (contentType != null) {
            exchange.getResponseHeaders().add("Content-Type", contentType);
        }
        exchange.sendResponseHeaders(code, contentType == null ? -1 : bytes.length);
        if (contentType != null && bytes.length > 0) {
            exchange.getResponseBody().write(bytes);
        }
        exchange.close();
    }

    public static String getQueryValue(@NotNull HttpExchange exchange, @NotNull String key) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) return null;
        String[] params = query.split("&");
        for (String param : params) {
            String[] pair = param.split("=");
            if (pair.length > 0 && pair[0].equals(key)) {
                return pair.length == 1 ? "" : pair[1];
            }
        }
        return null;
    }

    @Contract("null, _ -> false")
    public static boolean getBool(@Nullable JsonObject object, String key) {
        return object != null && object.has(key) && object.get(key).getAsBoolean();
    }

    public static boolean handleCors(@NotNull HttpExchange exchange, @NotNull String allowedOrigin, @NotNull String allowedMethods) throws IOException {
        String origin = exchange.getRequestHeaders().getFirst("Origin");
        if (allowedOrigin.equalsIgnoreCase(origin)) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", origin);
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, authorization");
            exchange.getResponseHeaders().add("Access-Control-Max-Age", "86400");
        }
        exchange.getResponseHeaders().add("Allow", "OPTIONS, " + allowedMethods);
        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            WebServerUtils.send(exchange, HttpURLConnection.HTTP_NO_CONTENT, null, new byte[0]);
            return true;
        }
        return false;
    }
}
