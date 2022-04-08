package com.soilhumidity.backend.util.service.notification.mail;

import com.soilhumidity.backend.util.service.notification.ILocalizedNotification;

public class LocalizedTextMail extends TextMail implements ILocalizedNotification {
    public LocalizedTextMail(String recipient, String subject, String message) {
        super(recipient, subject, message);
    }
}
