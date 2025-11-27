package com.groupeisi.m2gl.config;

import com.groupeisi.m2gl.websocket.SimpleWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configuration WebSocket simple (sans STOMP) pour la compatibilité avec Postman.
 */
@Configuration
@EnableWebSocket
public class SimpleWebSocketConfig implements WebSocketConfigurer {

    private final SimpleWebSocketHandler simpleWebSocketHandler;

    public SimpleWebSocketConfig(SimpleWebSocketHandler simpleWebSocketHandler) {
        this.simpleWebSocketHandler = simpleWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Endpoint simple pour Postman (sans STOMP)
        registry.addHandler(simpleWebSocketHandler, "/ws-simple").setAllowedOriginPatterns("*");
    }
}

