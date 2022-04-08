package com.soilhumidity.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soilhumidity.backend.enums.EClientId;
import com.soilhumidity.backend.util.service.notification.websocket.SocketRegistry;
import com.soilhumidity.backend.util.service.notification.websocket.WebSocketHandShakeInterceptor;
import com.soilhumidity.backend.util.service.notification.websocket.event.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.soilhumidity.backend.config.security.JwtUtil;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${server.websocket.baseUrl}")
    private String wsBaseUrl;

    private final JwtUtil jwtUtil;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final SocketRegistry socketRegistry;

    private final EClientId clientId;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(webSocketHandler(), wsBaseUrl + "/connect")
                .setAllowedOrigins("*")
                .addInterceptors(webSocketHandShakeInterceptor());
    }

    private WebSocketHandShakeInterceptor webSocketHandShakeInterceptor() {
        return new WebSocketHandShakeInterceptor(jwtUtil, clientId);
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketService(new ObjectMapper(), applicationEventPublisher, socketRegistry);
    }

}
