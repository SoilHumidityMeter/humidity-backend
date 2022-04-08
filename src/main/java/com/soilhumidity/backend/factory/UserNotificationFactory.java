package com.soilhumidity.backend.factory;

import com.soilhumidity.backend.dto.notification.UserNotificationDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import com.soilhumidity.backend.model.UserNotification;

@Component
@NoArgsConstructor
public class UserNotificationFactory {

    public UserNotificationDto createUserNotificationDto(UserNotification userNotification) {
        return new UserNotificationDto(
                userNotification.getId(),
                userNotification.getOneSignalNotification().getCreatedOn(),
                userNotification.getOneSignalNotification().getContent(),
                userNotification.getOneSignalNotification().getHeading(),
                userNotification.getOneSignalNotification().getUser().getEmail(),
                userNotification.isRead()
        );
    }
}
