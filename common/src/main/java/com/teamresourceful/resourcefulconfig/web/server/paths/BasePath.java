package com.teamresourceful.resourcefulconfig.web.server.paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;
import com.teamresourceful.resourcefulconfig.web.utils.WebServerUtils;
import com.teamresourceful.resourcefulconfig.web.utils.WebVerifier;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.HttpURLConnection;

@ApiStatus.Internal
public interface BasePath extends HttpHandler {

    @Override
    default void handle(HttpExchange exchange) throws IOException {
        if (!WebServerUtils.handleCors(exchange, verifier().origin(), method())) {
            if (exchange.getRequestMethod().equalsIgnoreCase(method())) {
                final UserJwtPayload payload = verifier().getInfo(exchange);
                if (payload != null) {
                    handleCall(exchange, payload);
                } else {
                    WebServerUtils.send(exchange, HttpURLConnection.HTTP_UNAUTHORIZED, null, new byte[0]);
                }
            } else {
                WebServerUtils.send(exchange, HttpURLConnection.HTTP_BAD_REQUEST, null, new byte[0]);
            }
        }
    }

    void handleCall(HttpExchange exchange, UserJwtPayload payload) throws IOException;

    WebVerifier verifier();

    String method();
}