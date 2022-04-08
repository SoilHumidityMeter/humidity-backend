package com.soilhumidity.backend.service;

import com.soilhumidity.backend.dto.PageFilter;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.message.BasicResponse;
import com.soilhumidity.backend.dto.notification.WebNotificationDto;
import com.soilhumidity.backend.enums.EErrorCode;
import com.soilhumidity.backend.enums.EWebSocketEvent;
import com.soilhumidity.backend.event.OneSignalNotificationEvent;
import com.soilhumidity.backend.util.service.notification.NotificationException;
import com.soilhumidity.backend.util.service.notification.helper.MultiLanguageText;
import com.soilhumidity.backend.util.service.notification.onesignal.OneSignalService;
import com.soilhumidity.backend.util.service.notification.onesignal.helper.OneSignalCreateNotificationResult;
import com.soilhumidity.backend.util.service.notification.websocket.WebSocketMessage;
import com.soilhumidity.backend.util.service.notification.websocket.WebSocketPayload;
import com.soilhumidity.backend.util.service.notification.websocket.event.WebSocketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.soilhumidity.backend.config.security.JwtUtil;
import com.soilhumidity.backend.factory.NotificationFactory;
import com.soilhumidity.backend.model.MobileDevice;
import com.soilhumidity.backend.model.OneSignalNotification;
import com.soilhumidity.backend.model.User;
import com.soilhumidity.backend.repository.MobileDeviceRepository;
import com.soilhumidity.backend.repository.OneSignalNotificationRepository;
import com.soilhumidity.backend.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private final OneSignalService oneSignalService;
    private final WebSocketService webSocketService;
    private final MessageSourceAccessor messageSource;
    private final NotificationFactory notificationFactory;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OneSignalNotificationRepository oneSignalNotificationRepository;
    private final MobileDeviceRepository mobileDeviceRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Response<BasicResponse> sendMessageToAll(String content, String heading, String token) {

        var user = userRepository.getById(jwtUtil.getUserId(token));

        var notification = notificationFactory
                .createWebNotification(content, heading, user);

        var allUsers = mobileDeviceRepository.findAll().stream()
                .map(MobileDevice::getUser).collect(Collectors.toSet());

        return handlePushNotification(notification, allUsers);
    }

    private Response<BasicResponse> handlePushNotification(OneSignalNotification notification, Set<User> recipents) {

        var mobileNotification = notificationFactory.createPersonalNotification(notification, recipents);

        if (mobileNotification == null) {
            return Response.notOk(messageSource.getMessage("push_notification.no_player_found"),
                    EErrorCode.BAD_REQUEST);
        }

        try {

            var notificationResult = (OneSignalCreateNotificationResult) oneSignalService.send(mobileNotification);

            applicationEventPublisher.publishEvent(
                    new OneSignalNotificationEvent(this, recipents, notification, notificationResult));

            return Response.ok(new BasicResponse(messageSource.getMessage("send_all.push_notification.success")));

        } catch (NotificationException e) {

            log.error("NotificationService line 84 " + e.getMessage() + " " + e.getCause());
            return Response.notOk(messageSource.getMessage("send_all.push_notification.fail"),
                    EErrorCode.BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public Response<Page<WebNotificationDto>> getSentMessages(@NotNull PageFilter pageFilter, String token) {

        var pagedMessages = oneSignalNotificationRepository
                .findAll(pageFilter.asPageable());
        //TODO look here
        return Response.ok(pagedMessages
                .map(notificationFactory::createWebNotificationDto));

    }

    public Response<BasicResponse> sendMessageToAllWs(String content, Set<String> recipients) {

        try {
            webSocketService.send(
                    new WebSocketMessage(recipients,
                            WebSocketPayload.of(EWebSocketEvent.NOTIFICATION,
                                    new MultiLanguageText(content)))
            );

            return Response.ok(BasicResponse.of("Başarılı"));

        } catch (NotificationException e) {
            log.error("NotificationService line 112 notification service " + e.getMessage());
            return Response.notOk("Başarısız", EErrorCode.BAD_REQUEST);
        }
    }
}
