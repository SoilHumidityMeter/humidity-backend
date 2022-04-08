package com.soilhumidity.backend.util.service.notification.websocket;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.soilhumidity.backend.util.service.notification.INotification;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage implements INotification<Set<String>, WebSocketPayload> {

    private Set<String> recipient;

    private WebSocketPayload message;

    @Override
    public Set<String> getRecipient() {
        return recipient;
    }

    @Override
    public WebSocketPayload getMessage() {
        return message;
    }
}
