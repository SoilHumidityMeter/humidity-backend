package com.soilhumidity.backend.validator;

import com.soilhumidity.backend.repository.UserNotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserNotificationValidator {

    private final UserNotificationRepository userNotificationRepository;
    private final MessageSourceAccessor messageSource;

    public ValidationResult validate(Long userId, Long messageId) {

        var maybeNotification = userNotificationRepository.findById(messageId);

        if (maybeNotification.isEmpty()) {
            return ValidationResult.failed(messageSource
                    .getMessage("user_notification.id.not_found"));
        }

        if (!maybeNotification.get().getUser().getId().equals(userId)) {
            return ValidationResult.failed(messageSource
                    .getMessage("user_notification.user_id.not_accessible"));
        }

        return ValidationResult.success();
    }
}
