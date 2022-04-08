package com.soilhumidity.backend.service;

import com.soilhumidity.backend.repository.OneSignalNotificationRepository;
import com.soilhumidity.backend.repository.UserNotificationRepository;
import com.soilhumidity.backend.util.service.notification.INotificationResult;
import com.soilhumidity.backend.util.service.notification.onesignal.helper.OneSignalCreateNotificationResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.soilhumidity.backend.model.Notification;
import com.soilhumidity.backend.model.OneSignalNotification;
import com.soilhumidity.backend.model.User;
import com.soilhumidity.backend.model.UserNotification;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OneSignalNotificationService {

    private final UserNotificationRepository userNotificationRepository;
    private final OneSignalNotificationRepository oneSignalNotificationRepository;

    @Transactional
    public void saveNotifications(Notification notification, Set<User> users, INotificationResult result) {

        if (!(notification instanceof OneSignalNotification) || !(result instanceof OneSignalCreateNotificationResult)) {
            return;
        }

        var oneSignalResult = (OneSignalCreateNotificationResult) result;
        var notificationWithOneSignalId = (OneSignalNotification) notification;

        notificationWithOneSignalId.setOneSignalId(oneSignalResult.getId());

        var savedNotification = oneSignalNotificationRepository.save(notificationWithOneSignalId);

        var userNotifications = users.stream()
                .map(user -> new UserNotification(user, savedNotification))
                .collect(Collectors.toList());

        userNotificationRepository.saveAll(userNotifications);
    }
}
