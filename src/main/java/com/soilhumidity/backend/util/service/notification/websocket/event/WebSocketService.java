package com.soilhumidity.backend.util.service.notification.websocket.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.soilhumidity.backend.util.service.notification.websocket.message_handler.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.soilhumidity.backend.util.service.notification.INotificationResult;
import com.soilhumidity.backend.util.service.notification.INotificationService;
import com.soilhumidity.backend.util.service.notification.NotificationException;
import com.soilhumidity.backend.util.service.notification.websocket.SocketRegistry;
import com.soilhumidity.backend.util.service.notification.websocket.WebSocketMessage;
import com.soilhumidity.backend.util.service.notification.websocket.WebSocketNotificationResult;
import com.soilhumidity.backend.util.service.notification.websocket.message_handler.IMessageHandler;
import com.soilhumidity.backend.util.service.notification.websocket.message_handler.PingMessageHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WebSocketService extends TextWebSocketHandler implements INotificationService<WebSocketMessage> {

    private final ObjectWriter objectWriter;

    private final ObjectMapper objectMapper;

    private final ApplicationEventPublisher eventPublisher;

    private final SocketRegistry socketRegistry;

    private final Map<String, IMessageHandler> handlers = Map.of(
            "ping", new PingMessageHandler()
    );

    public WebSocketService(@NotNull ObjectMapper mapper, ApplicationEventPublisher publisher, SocketRegistry registry) {
        objectWriter = mapper.writerWithDefaultPrettyPrinter();
        objectMapper = mapper;
        eventPublisher = publisher;
        socketRegistry = registry;
    }

    @Override
    public INotificationResult send(WebSocketMessage notification) throws NotificationException {

        Set<WebSocketSession> sessions;
        
        if (notification.getRecipient().isEmpty()) {
            sessions = socketRegistry.getSessions();
        } else {
            sessions = socketRegistry.getSessions(notification.getRecipient());
        }


        TextMessage message;
        try {
            message = new TextMessage(objectWriter.writeValueAsString(notification.getMessage()));
        } catch (JsonProcessingException e) {
            log.error("WebSocketService at line 63 for object write: " + e.getMessage());
            message = new TextMessage("");
        }

        TextMessage finalMessage = message;

        sessions.forEach(session -> {
            try {
                session.sendMessage(finalMessage);
                log.info("Send message to socketId: {}", session.getId());

            } catch (IOException e) {
                log.error("WebSocketNotificationService at line 28: " + e.getMessage());
            }
        });

        return WebSocketNotificationResult.success();
    }

    @Override
    public List<INotificationResult> send(List<WebSocketMessage> notifications) throws NotificationException {

        TextMessage messages;
        try {
            messages = new TextMessage(objectWriter.writeValueAsString(notifications
                    .stream()
                    .map(WebSocketMessage::getMessage)
                    .collect(Collectors.toList())));
        } catch (JsonProcessingException e) {
            log.error("WebSocketNotificationService at line 45 for object write: " + e.getMessage());
            messages = new TextMessage("");
        }

        TextMessage finalMessages = messages;

        socketRegistry.getSessions().forEach(session -> {
            try {
                session.sendMessage(finalMessages);
                log.info("Send message to socketId: {}", session.getId());

            } catch (IOException e) {
                log.error("WebSocketNotificationService at line 28: " + e.getMessage());
            }
        });

        return Collections.singletonList(WebSocketNotificationResult.success());
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable throwable) {
        log.error("Error occured at sender " + session, throwable);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        log.info(String.format("Session %s closed because of %s", session.getId(), status.getReason()));
        eventPublisher.publishEvent(new WebSocketDisconnectEvent(this, session.getId()));
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        log.info(session.getId() + " connected with WebSocket");
        eventPublisher.publishEvent(new WebSocketConnectEvent(this, session));
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) {

        try {
            EventMessage msg = objectMapper.readValue(message.getPayload(), EventMessage.class);

            var handler = handlers.get(msg.getEvent());

            if (handler != null) {
                session.sendMessage(new TextMessage(objectWriter.writeValueAsString(handler.invoke())));
            } else {
                throw new UnsupportedOperationException("This event is not supported: " + msg.getEvent());
            }

        } catch (Exception e) {
            log.warn("Incoming message: {} with error {}", message, e.getMessage());
        }
    }
}
