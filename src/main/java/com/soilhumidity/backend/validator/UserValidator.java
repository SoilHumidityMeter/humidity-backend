package com.soilhumidity.backend.validator;

import com.soilhumidity.backend.config.MimeTypesConfig;
import com.soilhumidity.backend.dto.generic.UserCreateRequest;
import com.soilhumidity.backend.dto.user.UserUpdateRequest;
import com.soilhumidity.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.soilhumidity.backend.factory.ExampleFactory;

@Component
@AllArgsConstructor
public class UserValidator {
    private final MimeTypesConfig mimeTypesConfig;
    private final MessageSourceAccessor messageSource;
    private final UserRepository userRepository;
    private final ExampleFactory exampleFactory;

    public ValidationResult validate(MultipartFile multipartFile) {
        var isSupported = mimeTypesConfig
                .getValidImageMimeTypes()
                .contains(multipartFile.getContentType());

        return isSupported ?
                ValidationResult.success() :
                ValidationResult.failed(messageSource
                        .getMessage("update_profile_picture.mime_type.invalid"));
    }

    public ValidationResult validate(UserCreateRequest body) {
        var userExist = userRepository.exists(exampleFactory.createUser(body));

        if (userExist) {
            return ValidationResult.failed(messageSource.getMessage("user_create_dto.user.exists"));
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(Long id) {
        var maybeUser = userRepository.findById(id);

        if (maybeUser.isEmpty()) {
            return ValidationResult.failed(messageSource
                    .getMessage("user_update_dto.user.not_found"));
        }
        return ValidationResult.success();
    }

    public ValidationResult validate(Long id, UserUpdateRequest body) {
        var maybeUser = userRepository.findById(id);

        if (maybeUser.isEmpty()) {
            return ValidationResult.failed(messageSource
                    .getMessage("user_update_dto.user.not_found"));
        }

        var userCount = userRepository.count(exampleFactory.createUser(body));

        return userCount > 1 ?
                ValidationResult.failed(messageSource
                        .getMessage("user_create_dto.user.exists")) :
                ValidationResult.success();
    }
}

