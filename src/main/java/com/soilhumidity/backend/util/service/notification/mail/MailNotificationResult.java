package com.soilhumidity.backend.util.service.notification.mail;

import lombok.NoArgsConstructor;
import com.soilhumidity.backend.util.service.notification.INotificationResult;

@NoArgsConstructor
public class MailNotificationResult implements INotificationResult {

    public static MailNotificationResult success() {
        return new MailNotificationResult();
    }

}
