package com.soilhumidity.backend.util.service.notification.mail;

import com.soilhumidity.backend.util.service.notification.INotificationResult;
import com.soilhumidity.backend.util.service.notification.INotificationService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.soilhumidity.backend.config.MailConfig;
import com.soilhumidity.backend.util.AddressUtil;
import com.soilhumidity.backend.util.service.notification.ILocalizedNotification;
import com.soilhumidity.backend.util.service.notification.NotificationException;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class MailService implements INotificationService<Mail> {
    private final MessageSourceAccessor messageSource;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MailConfig mailConfig;

    private Address[] from() throws AddressException {
        return Collections.singletonList(AddressUtil.of(mailConfig.getFrom())).toArray(new Address[1]);
    }

    @Override
    public INotificationResult send(Mail mail) throws NotificationException {
        try {
            MimeMessage message = createMessage(mail);
            mailSender.send(message);
        } catch (NotificationException e) {
            throw e;
        } catch (Exception e) {
            throw new NotificationException("An error occurred during email creation.", e);
        }

        return MailNotificationResult.success();
    }

    @Override
    public List<INotificationResult> send(List<Mail> notifications) throws NotificationException {
        for (var notification : notifications) {
            send(notification);
        }

        return Collections.singletonList(MailNotificationResult.success());
    }

    private MimeMessage createMessage(@NotNull Mail message) throws NotificationException {
        if (message instanceof TextMail) {
            return createMessageFromTextMail((TextMail) message);
        } else if (message instanceof HtmlMail) {
            return createMessageFromHtmlMail((HtmlMail) message);
        } else {
            throw new NotificationException("Implementation not found for class " + message.getClass() + ".");
        }
    }


    private MimeMessage createMessageFromTextMail(@NotNull TextMail mail) throws NotificationException {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            message.addFrom(from());

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getRecipient()));

            if (mail instanceof ILocalizedNotification) {
                message.setSubject(messageSource.getMessage(mail.getSubject()));
            } else {
                message.setSubject(mail.getSubject());
            }

            message.setText(mail.getMessage(), "UTF-8");

            return message;
        } catch (MessagingException e) {
            throw new NotificationException("An error occurred during email creation.", e);
        }
    }

    private MimeMessage createMessageFromHtmlMail(@NotNull HtmlMail mail) throws NotificationException {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(mailConfig.getFrom());
            helper.setTo(mail.getRecipient());

            Context context;
            if (mail instanceof ILocalizedNotification) {
                helper.setSubject(messageSource.getMessage(mail.getSubject()));
                context = new Context(LocaleContextHolder.getLocale(), mail.getVariables());
            } else {
                helper.setSubject(mail.getSubject());
                context = new Context();
                context.setVariables(mail.getVariables());
            }

            String text = templateEngine.process(mail.getMessage(), context);
            helper.setText(text, true);

            return helper.getMimeMessage();
        } catch (MessagingException e) {
            throw new NotificationException("An error occurred during email creation.", e);
        }
    }
}
