package com.soilhumidity.backend.controller;

import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.auth.ProfileDto;
import com.soilhumidity.backend.dto.user.ProfilePictureUpdateDto;
import com.soilhumidity.backend.dto.user.UserDeviceDto;
import com.soilhumidity.backend.service.UserService;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Api(tags = UserController.TAG)
@ApiInformation(tag = UserController.TAG, description = "User related endpoints and CRUD operations")
@RequestMapping(UserController.TAG)
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserController {

    protected static final String TAG = "users";

    private final UserService userService;

    @GetMapping("me")
    @ApiOperation(value = "Get user's profile info.")
    public ResponseEntity<Response<ProfileDto>> getProfile(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userService.getProfile(token).toResponseEntity();
    }

    @PostMapping(
            path = "/me/device")
    @ApiOperation(value = "Add user device.")
    public ResponseEntity<Response<UserDeviceDto>> addUserDevice(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestParam String deviceId
    ) {
        return userService.addUserDevice(deviceId, token).toResponseEntity();
    }

    @GetMapping(
            path = "/me/device")
    @ApiOperation(value = "Get user devices.")
    public ResponseEntity<Response<List<UserDeviceDto>>> getUserDevices(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userService.getUserDevices(token).toResponseEntity();
    }

    @PutMapping(
            path = "/me/picture",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ApiOperation(value = "Update user's profile picture.")
    public ResponseEntity<Response<ProfilePictureUpdateDto>> getProfilePictureUrl(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token,
            @RequestPart("file") MultipartFile file
    ) {
        return userService.updateProfilePicture(token, file).toResponseEntity();
    }
}
