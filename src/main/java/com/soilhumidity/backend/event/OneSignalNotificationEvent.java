package com.soilhumidity.backend.event;

import com.soilhumidity.backend.model.Notification;
import com.soilhumidity.backend.model.User;
import com.soilhumidity.backend.util.service.notification.onesignal.helper.OneSignalCreateNotificationResult;

import java.util.Set;

public class OneSignalNotificationEvent extends NotificationEvent {

    public OneSignalNotificationEvent(Object source, Set<User> users, Notification notification,
                                      OneSignalCreateNotificationResult oneSignalCreateNotificationResult) {
        super(source, users, notification, oneSignalCreateNotificationResult);
    }
}
