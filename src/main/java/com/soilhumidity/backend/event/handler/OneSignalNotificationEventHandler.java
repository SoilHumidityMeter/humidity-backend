package com.soilhumidity.backend.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.soilhumidity.backend.event.NotificationEvent;
import com.soilhumidity.backend.service.OneSignalNotificationService;
import com.soilhumidity.backend.util.LoggableFuture;

@Component
@RequiredArgsConstructor
public class OneSignalNotificationEventHandler extends NotificationEventHandler {

    private final OneSignalNotificationService oneSignalNotificationService;

    @Override
    public void onNotificationSent(NotificationEvent event) {
        LoggableFuture.runAsync(() -> oneSignalNotificationService
                .saveNotifications(event.getNotification(), event.getUsers(), event.getResult()));
    }
}
