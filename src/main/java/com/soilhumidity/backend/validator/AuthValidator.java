package com.soilhumidity.backend.validator;

import com.soilhumidity.backend.dto.auth.*;
import com.soilhumidity.backend.enums.EClientId;
import com.soilhumidity.backend.enums.EGrantType;
import com.soilhumidity.backend.enums.ERole;
import com.soilhumidity.backend.enums.EVerificationKeyType;
import com.soilhumidity.backend.model.User;
import com.soilhumidity.backend.repository.UserRepository;
import com.soilhumidity.backend.repository.VerificationCodeRepository;
import com.soilhumidity.backend.util.SoilBackendRegex;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final EClientId eClientId;
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final MessageSourceAccessor messageSource;

    private Map<String, String> roleAndClientIdTable = null;

    @PostConstruct
    protected void init() {
        roleAndClientIdTable = Map.of(
                ERole.USER, eClientId.getMobileClientId(),
                ERole.SYSADMIN, eClientId.getWebClientId()
        );
    }

    public ValidationResult validate(@NotNull LoginRequest dto) {
        if (dto.getGrant_type() == EGrantType.password) {
            if (dto.getUsername() == null || dto.getUsername().isBlank()) {
                return ValidationResult.failed(
                        messageSource.getMessage("login_request.username.empty")
                );
            }

            if (!dto.getUsername().matches(SoilBackendRegex.EMAIL)) {
                return ValidationResult.failed(
                        messageSource.getMessage("validation.generic.email.unfit_regex")
                );
            }

            if (dto.getPassword() == null || dto.getPassword().isBlank()) {
                return ValidationResult.failed(
                        messageSource.getMessage("login_request.password.empty")
                );
            }
            if (eClientId.getMobileClientId().equals(dto.getClient_id()) &&
                    (dto.getClient_secret() == null || dto.getClient_secret().isBlank())) {
                return ValidationResult.failed(
                        messageSource.getMessage("login_request.client_secret.empty")
                );

            }
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull ForgotPasswordRequest dto) {
        var maybeUser = userRepository.findByEmail(dto.getEmail());

        if (maybeUser.isEmpty()) {
            return ValidationResult.failed(
                    messageSource.getMessage("forgot_password_request.user.not_found")
            );
        }

        var user = maybeUser.get();

        if (!user.isAccountNonExpired()) {
            return ValidationResult.failed(
                    messageSource.getMessage("forgot_password_request.user.unconfirmed")
            );
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull User user) {

        var maybeUser = userRepository.findByEmail(user.getUsername());

        if (maybeUser.isPresent()) {
            return ValidationResult.failed(
                    messageSource.getMessage("register_request.email.exist")
            );
        }

        if (user.getPhoneNumber() != null && userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            return ValidationResult.failed(
                    messageSource.getMessage("register_request.phone.exist")
            );
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull ResetPasswordRequest body) {
        var maybeVerificationCode = verificationCodeRepository.findByCode(body.getCode());

        if (maybeVerificationCode.isEmpty()) {
            return ValidationResult.failed(
                    messageSource.getMessage("reset_password_request.code.not_found")
            );
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull ResendActivationCodeRequest dto) {
        var maybeUser = userRepository.findByEmail(dto.getEmail());
        if (maybeUser.isEmpty()) {
            return ValidationResult.failed(
                    messageSource.getMessage("resend_activation_request.user.not_found")
            );
        }

        var user = maybeUser.get();

        if (user.isAccountNonExpired()) {
            return ValidationResult.failed(
                    messageSource.getMessage("resend_activation_request.user.confirmed")
            );
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull AccountActivationRequest body) {
        var maybeVerificationCode = verificationCodeRepository.findByCode(body.getCode());

        if (maybeVerificationCode.isEmpty()) {
            return ValidationResult.failed(
                    messageSource.getMessage("activation.code.not_found")
            );
        }

        var verificationCode = maybeVerificationCode.get();

        if (!verificationCode.getType().equals(EVerificationKeyType.ACTIVATION)) {
            return ValidationResult.failed(
                    messageSource.getMessage("activation.code.wrong_type")
            );
        }

        if (verificationCode.isExpired()) {
            return ValidationResult.failed(
                    messageSource.getMessage("activation.code.expired")
            );
        }

        var user = verificationCode.getUser();
        if (!ERole.isUser(user.getRole()) && body.getPassword() == null) {
            return ValidationResult.failed(
                    messageSource.getMessage("activation.password.required")
            );
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull User user, @NotNull String clientId) {
        var role = ERole.stringValueOf(user.getRole());

        if (!clientId.equals(roleAndClientIdTable.getOrDefault(role, null))) {
            return ValidationResult.failed(
                    messageSource.getMessage("login_request.client_id.not_appropriate")
            );
        }

        return ValidationResult.success();
    }

    public boolean isMobileUser(String client_id) {
        return client_id.equals(eClientId.getMobileClientId());
    }
}
