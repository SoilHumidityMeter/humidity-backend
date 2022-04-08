package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.dto.PageFilter;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.message.BasicResponse;
import com.soilhumidity.backend.dto.notification.WebNotificationDto;
import com.soilhumidity.backend.enums.ERole;
import com.soilhumidity.backend.service.NotificationService;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Set;

@RestController
@Api(tags = NotificationController.TAG)
@RequestMapping(NotificationController.TAG)
@ApiInformation(tag = NotificationController.TAG, description = "Send notifications to users and all users")
@AllArgsConstructor
@RolesAllowed(ERole.SYSADMIN)
public class NotificationController {

    protected static final String TAG = "notifications";

    private final NotificationService notificationService;

    @PostMapping("all")
    @ApiOperation(value = "Send notification to all users")
    public ResponseEntity<Response<BasicResponse>> sendNotificationToAllUsers(
            @RequestParam String content,
            @RequestParam String heading,
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return notificationService.sendMessageToAll(content, heading, token).toResponseEntity();
    }

    @PostMapping("all/ws")
    @ApiOperation(value = "Send notification to all users via ws")
    public ResponseEntity<Response<BasicResponse>> sendWsNotificationToAllUsers(
            @RequestParam String content,
            @RequestParam Set<String> recipients
    ) {
        return notificationService.sendMessageToAllWs(content, recipients).toResponseEntity();
    }

    @GetMapping
    @ApiOperation(value = "Get sent notifications")
    public ResponseEntity<Response<Page<WebNotificationDto>>> getNotifications(
            PageFilter pageFilter,
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return notificationService.getSentMessages(pageFilter, token).toResponseEntity();
    }
}
