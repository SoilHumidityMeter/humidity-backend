package com.soilhumidity.backend.util.service.notification.websocket.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.Assert;

public abstract class WebSocketEvent extends ApplicationEvent {

    @Getter
    private final String webSocketSessionId;

    protected WebSocketEvent(Object source, String webSocketSessionId) {
        super(source);
        Assert.notNull(webSocketSessionId, "Web socket session id must not be null");
        this.webSocketSessionId = webSocketSessionId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + webSocketSessionId + "]";
    }
}
