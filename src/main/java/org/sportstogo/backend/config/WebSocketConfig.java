package org.sportstogo.backend.config;

import org.sportstogo.backend.WebSocketHandlers.ChatWebSocketHandler;
import org.sportstogo.backend.WebSocketHandlers.ProtocolAuthHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(chatWebSocketHandler, "/social/chat")
                .addInterceptors(new ProtocolAuthHandshakeInterceptor())
                .setAllowedOrigins(allowedOrigins.split(","));

    }
}