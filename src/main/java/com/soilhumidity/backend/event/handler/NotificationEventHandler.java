package com.soilhumidity.backend.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.soilhumidity.backend.event.NotificationEvent;

@Component
@RequiredArgsConstructor
public abstract class NotificationEventHandler {

    @EventListener
    public abstract void onNotificationSent(NotificationEvent event);
}
