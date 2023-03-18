package com.teamresourceful.resourcefulconfig.web.server;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import com.sun.net.httpserver.HttpServer;
import com.teamresourceful.resourcefulconfig.common.utils.ModUtils;
import com.teamresourceful.resourcefulconfig.web.config.WebServerConfig;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;
import com.teamresourceful.resourcefulconfig.web.server.paths.GetConfigPath;
import com.teamresourceful.resourcefulconfig.web.server.paths.GetConfigsPath;
import com.teamresourceful.resourcefulconfig.web.server.paths.PostSavePath;
import com.teamresourceful.resourcefulconfig.web.utils.WebServerUtils;
import com.teamresourceful.resourcefulconfig.web.utils.WebVerifier;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@ApiStatus.Internal
public final class WebServer {

    private static WebServer instance;

    public static final Logger LOGGER = LogUtils.getLogger();

    private final WebServerConfig config;

    private WebServer() {
        this.config = loadConfig();
        loadServer(config);
    }

    private static WebServerConfig loadConfig() {
        File file = new File(ModUtils.getConfigPath().toFile(), "resourceful-config-web.json");
        if (!file.exists()) {
            WebServerConfig.CODEC.encodeStart(JsonOps.INSTANCE, WebServerConfig.DEFAULT).result().ifPresent(json -> {
                try {
                    Files.writeString(file.toPath(), WebServerUtils.GSON_PRETTY.toJson(json), StandardCharsets.UTF_8);
                } catch (IOException ignored) {}
            });
            return WebServerConfig.DEFAULT;
        } else {
            try {
                final String data = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                final JsonObject object = WebServerUtils.GSON.fromJson(data, JsonObject.class);

                return WebServerConfig.CODEC.parse(JsonOps.INSTANCE, object).get().map(s -> s, error -> {
                    LOGGER.error("Failed to load config file: ${}", error);
                    return WebServerConfig.DEFAULT;
                });
            } catch (IOException e) {
                LOGGER.error("Failed to load config file", e);
                return WebServerConfig.DEFAULT;
            }
        }
    }

    private static void loadServer(WebServerConfig config) {
        if (config.enabled()) {
            try {
                final WebVerifier verifier = new WebVerifier(config);
                HttpServer server = HttpServer.create(new InetSocketAddress(config.port()), 0);
                server.createContext("/configs", new GetConfigsPath(verifier));
                server.createContext("/config", new GetConfigPath(verifier));
                server.createContext("/save", new PostSavePath(verifier));
                server.start();
                LOGGER.info("Resourceful Config Web Server started");
            } catch (IOException e) {
                LOGGER.error("Failed to start Resourceful Config Web Server", e);
            }
        }
    }

    public static void start() {
        if (instance == null) {
            instance = new WebServer();
        }
    }

    public static boolean verify(UserJwtPayload info) {
        return instance != null && instance.config.valid(info);
    }
}