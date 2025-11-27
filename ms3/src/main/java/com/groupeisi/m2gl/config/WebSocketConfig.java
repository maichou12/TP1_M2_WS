package com.groupeisi.m2gl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration WebSocket pour la communication en temps réel.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Active un simple broker de messages en mémoire pour envoyer des messages aux clients
        config.enableSimpleBroker("/topic", "/queue");
        // Préfixe pour les messages destinés aux méthodes annotées @MessageMapping
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint pour la connexion WebSocket
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
        // Endpoint alternatif sans SockJS
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }
}

