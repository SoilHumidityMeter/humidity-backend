package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.dto.PageFilter;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.message.BasicResponse;
import com.soilhumidity.backend.dto.notification.UserNotificationDto;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.soilhumidity.backend.service.UserNotificationService;

@RestController
@Api(tags = UserNotificationController.TAG)
@ApiInformation(tag = UserNotificationController.TAG, description = "Get user notifications and crud operations")
@RequestMapping(NotificationController.TAG)
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserNotificationController {

    protected static final String TAG = "user-notifications";

    private final UserNotificationService userNotificationService;

    @GetMapping("me")
    @ApiOperation(value = "Get user notifications")
    public ResponseEntity<Response<Page<UserNotificationDto>>> getUserNotifications(
            PageFilter pageFilter,
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userNotificationService.getUserMessages(pageFilter, token).toResponseEntity();
    }

    @GetMapping("me/{id}")
    @ApiOperation(value = "Get user notification by one signal id")
    public ResponseEntity<Response<UserNotificationDto>> getUserNotification(
            @PathVariable String id,
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userNotificationService.getUserNotification(id, token).toResponseEntity();
    }

    @GetMapping("me/unread")
    @ApiOperation(value = "Get users unread notification count")
    public ResponseEntity<Response<Long>> getUserUnreadNotificationCount(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userNotificationService.getUserUnreadNotificationCount(token).toResponseEntity();
    }

    @PatchMapping("me/{id}")
    @ApiOperation(value = "Read user notification")
    public ResponseEntity<Response<BasicResponse>> readNotification(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable Long id
    ) {
        return userNotificationService.readNotification(id, token).toResponseEntity();
    }

    @DeleteMapping("me/{id}")
    @ApiOperation(value = "Delete user notification")
    public ResponseEntity<Response<BasicResponse>> deleteNotification(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable Long id
    ) {
        return userNotificationService.deleteNotification(id, token).toResponseEntity();
    }
}
