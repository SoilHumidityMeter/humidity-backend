package com.soilhumidity.backend.util.service.notification.websocket.event;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketConnectEvent extends WebSocketEvent {

    @Getter
    private final WebSocketSession webSocketSession;
    
    protected WebSocketConnectEvent(Object source, WebSocketSession webSocketSession) {
        super(source, webSocketSession.getId());
        this.webSocketSession = webSocketSession;
    }

}
