package com.soilhumidity.backend.factory;

import com.soilhumidity.backend.dto.notification.WebNotificationDto;
import com.soilhumidity.backend.util.service.notification.helper.MultiLanguageText;
import com.soilhumidity.backend.util.service.notification.onesignal.MobileNotification;
import com.soilhumidity.backend.util.service.notification.onesignal.PersonalMobileNotification;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import com.soilhumidity.backend.model.Notification;
import com.soilhumidity.backend.model.OneSignalNotification;
import com.soilhumidity.backend.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@NoArgsConstructor
public class NotificationFactory {

    public MobileNotification createPersonalNotification(Notification notification, Set<User> recipents) {

        List<String> playerIds = new ArrayList<>();

        recipents.forEach(user -> {
            Hibernate.initialize(user.getMobileDevices());

            user.getMobileDevices()
                    .forEach(device -> playerIds.add(device.getDeviceId()));
        });

        if (playerIds.isEmpty()) {
            return null;
        }

        return new PersonalMobileNotification(playerIds, new MultiLanguageText(notification.getContent()),
                new MultiLanguageText(notification.getHeading()));
    }

    public WebNotificationDto createWebNotificationDto(OneSignalNotification oneSignalNotification) {
        return new WebNotificationDto(oneSignalNotification.getCreatedOn(), oneSignalNotification.getContent(),
                oneSignalNotification.getHeading(), oneSignalNotification.getUser().getEmail()
        );
    }

    public OneSignalNotification createWebNotification(String content, String heading, User user
    ) {
        return new OneSignalNotification(user, heading, content);
    }
}
