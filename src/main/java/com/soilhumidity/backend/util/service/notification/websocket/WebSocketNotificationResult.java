package com.soilhumidity.backend.util.service.notification.websocket;

import lombok.NoArgsConstructor;
import com.soilhumidity.backend.util.service.notification.INotificationResult;

@NoArgsConstructor
public class WebSocketNotificationResult implements INotificationResult {

    public static WebSocketNotificationResult success() {
        return new WebSocketNotificationResult();

    }
}
