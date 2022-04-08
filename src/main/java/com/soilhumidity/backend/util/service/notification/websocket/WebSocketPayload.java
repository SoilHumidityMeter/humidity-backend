package com.soilhumidity.backend.util.service.notification.websocket;

import com.soilhumidity.backend.enums.EWebSocketEvent;
import lombok.Getter;

@Getter
public class WebSocketPayload {

    private final IWebSocketNotification message;

    private final EWebSocketEvent event;

    private WebSocketPayload(IWebSocketNotification message, EWebSocketEvent event) {
        this.message = message;
        this.event = event;
    }

    public static WebSocketPayload of(EWebSocketEvent event, IWebSocketNotification message) {
        return new WebSocketPayload(message, event);
    }

}
