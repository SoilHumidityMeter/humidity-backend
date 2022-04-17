package com.soilhumidity.backend.factory;

import com.soilhumidity.backend.config.AppConfig;
import com.soilhumidity.backend.dto.auth.ProfileDto;
import com.soilhumidity.backend.dto.auth.RegisterDto;
import com.soilhumidity.backend.dto.auth.RegisterRequest;
import com.soilhumidity.backend.dto.generic.UserCreateRequest;
import com.soilhumidity.backend.dto.user.UserDeviceDto;
import com.soilhumidity.backend.dto.user.UserUpdateRequest;
import com.soilhumidity.backend.enums.ERole;
import com.soilhumidity.backend.model.User;
import com.soilhumidity.backend.model.UserDevice;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class UserFactory {
    private final BCryptPasswordEncoder encoder;
    private final AppConfig appConfig;

    private String getDefaultProfilePictureUrl() {
        return appConfig.getUrl() + "/static/img/profile-picture.png";
    }

    public User createUser(@NotNull RegisterRequest dto) {
        return new User(
                dto.getName(),
                dto.getSurname(),
                dto.getEmail().toLowerCase(Locale.US),
                dto.getPhone(),
                encoder.encode(dto.getPassword()),
                ERole.valueOf(ERole.USER),
                getDefaultProfilePictureUrl()
        );
    }

    public User createUser(@NotNull UserCreateRequest dto) {
        return new User(
                dto.getName(),
                dto.getSurname(),
                dto.getEmail().toLowerCase(Locale.US),
                dto.getPhone(),
                encoder.encode(UUID.randomUUID().toString()),
                ERole.valueOf(ERole.ADMIN),
                getDefaultProfilePictureUrl()
        );
    }

    public ProfileDto createProfileDto(@NotNull User user) {
        return new ProfileDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedOn(),
                user.getProfilePicture(),
                ERole.stringValueOf(user.getRole()),
                user.getUserDevices().stream().map(this::createUserDeviceDto).collect(Collectors.toList())
        );
    }

    public RegisterDto createRegisterDto(User user) {
        return new RegisterDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getUsername(),
                user.getPhoneNumber()
        );
    }

    private String extractRoleName(int roleId) {
        var role = ERole.stringValueOf(roleId);
        return role.substring(role.indexOf("_") + 1);
    }

    public User createUser(User user, UserUpdateRequest body) {
        return new User(
                user.getId(),
                body.getName(),
                body.getSurname(),
                body.getEmail().toLowerCase(Locale.US),
                body.getPhone(),
                user.getPassword(),
                user.getRole(),
                user.getProfilePicture(),
                user.getCreatedOn(),
                !user.isAccountNonLocked(),
                user.isAccountNonExpired()
        );
    }

    public UserDeviceDto createUserDeviceDto(UserDevice saved) {
        return new UserDeviceDto(
                saved.getDeviceId(),
                saved.getId(),
                saved.getCreatedAt()
        );
    }
}
