package com.soilhumidity.backend.service;

import com.soilhumidity.backend.config.security.JwtUtil;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.auth.ProfileDto;
import com.soilhumidity.backend.dto.user.ProfilePictureUpdateDto;
import com.soilhumidity.backend.dto.user.UserDeviceDto;
import com.soilhumidity.backend.enums.EErrorCode;
import com.soilhumidity.backend.factory.UserFactory;
import com.soilhumidity.backend.model.UserDevice;
import com.soilhumidity.backend.repository.UserDeviceRepository;
import com.soilhumidity.backend.repository.UserRepository;
import com.soilhumidity.backend.util.ImageUtils;
import com.soilhumidity.backend.util.service.storage.IStorageService;
import com.soilhumidity.backend.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final UserFactory userFactory;
    private final JwtUtil jwtTokenUtil;
    private final IStorageService storageService;
    private final MessageSourceAccessor messageSource;
    private final UserDeviceRepository userDeviceRepository;

    @Transactional(readOnly = true)
    public Response<ProfileDto> getProfile(@NotNull String token) {
        var user = userRepository.getById(jwtTokenUtil.getUserId(token));
        Hibernate.initialize(user.getUserDevices());
        return Response.ok(userFactory.createProfileDto(user));
    }

    public Response<ProfilePictureUpdateDto> updateProfilePicture(String token, MultipartFile file) {
        var userId = jwtTokenUtil.getUserId(token);
        var validation = userValidator.validate(file);

        if (validation.isNotValid()) {
            return Response.notOk(validation.getMessage(), EErrorCode.BAD_REQUEST);
        }

        var maybeUser = userRepository.findById(userId);

        if (maybeUser.isEmpty()) {
            return Response.notOk(messageSource.getMessage("update_profile_picture.user.not_found"),
                    EErrorCode.BAD_REQUEST);
        }

        var user = maybeUser.get();

        // Resize image and convert to InputStream.
        URL previousUrl = null;
        try {
            previousUrl = new URL(user.getProfilePicture());
        } catch (MalformedURLException ignored) {
            // This exception is tenable because removing old data is not relevant to user.
        }

        try {
            var image = ImageUtils.resizeImage(
                    ImageIO.read(file.getInputStream()),
                    128, 128
            );
            var is = ImageUtils.toInputStream(image);

            // Upload image.
            var url = storageService.put(is).toExternalForm();

            // Update profile picture.
            user.setProfilePicture(url);
            userRepository.saveAndFlush(user);

            // Delete previous picture if it is valid.
            if (previousUrl != null) {
                storageService.delete(previousUrl);
            }

            return Response.ok(new ProfilePictureUpdateDto(url));
        } catch (IOException e) {
            return Response.notOk(messageSource
                    .getMessage("update_profile_picture.io.fail"), EErrorCode.UNHANDLED);
        }
    }

    @Transactional
    public Response<UserDeviceDto> addUserDevice(String deviceId, String token) {
        var userId = jwtTokenUtil.getUserId(token);
        var validation = userDeviceRepository.existsByDeviceId(deviceId);

        if (validation) {
            return Response.notOk("Device already exists", EErrorCode.BAD_REQUEST);
        }

        var maybeUser = userRepository.findById(userId);

        if (maybeUser.isEmpty()) {
            return Response.notOk(messageSource.getMessage("update_profile_picture.user.not_found"),
                    EErrorCode.BAD_REQUEST);
        }

        var user = maybeUser.get();

        var saved = userDeviceRepository.save(new UserDevice(deviceId, user));

        return Response.ok(userFactory.createUserDeviceDto(saved));
    }

    @Transactional(readOnly = true)
    public Response<List<UserDeviceDto>> getUserDevices(String token) {
        var userId = jwtTokenUtil.getUserId(token);
        var user = userRepository.getById(userId);

        Hibernate.initialize(user.getUserDevices());
        
        return Response.ok(user.getUserDevices().stream().map(userFactory::createUserDeviceDto).collect(
                java.util.stream.Collectors.toList()
        ));
    }
}
