package com.soilhumidity.backend.util.service.notification.websocket.event;

public class WebSocketDisconnectEvent extends WebSocketEvent {
    protected WebSocketDisconnectEvent(Object source, String webSocketSessionId) {
        super(source, webSocketSessionId);
    }
}
