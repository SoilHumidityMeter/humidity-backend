package com.soilhumidity.backend.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import com.soilhumidity.backend.model.Notification;
import com.soilhumidity.backend.model.User;
import com.soilhumidity.backend.util.service.notification.INotificationResult;

import java.util.Set;

@Getter
public class NotificationEvent extends ApplicationEvent {

    private final Set<User> users;
    private final Notification notification;
    private final INotificationResult result;

    public NotificationEvent(Object source, Set<User> users, Notification notification, INotificationResult result) {
        super(source);
        this.users = users;
        this.notification = notification;
        this.result = result;
    }
}
