package com.soilhumidity.backend.service;

import com.soilhumidity.backend.config.security.JwtUtil;
import com.soilhumidity.backend.dto.Response;
import com.soilhumidity.backend.dto.auth.*;
import com.soilhumidity.backend.enums.EErrorCode;
import com.soilhumidity.backend.enums.ERole;
import com.soilhumidity.backend.event.RegistrationCompleteEvent;
import com.soilhumidity.backend.factory.UserFactory;
import com.soilhumidity.backend.model.MobileDevice;
import com.soilhumidity.backend.model.User;
import com.soilhumidity.backend.repository.MobileDeviceRepository;
import com.soilhumidity.backend.repository.UserRepository;
import com.soilhumidity.backend.repository.VerificationCodeRepository;
import com.soilhumidity.backend.util.LoggableFuture;
import com.soilhumidity.backend.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthValidator authValidator;
    private final VerificationCodeRepository verificationCodeRepository;
    private final UserFactory userFactory;
    private final BCryptPasswordEncoder encoder;
    private final MessageSourceAccessor messageSource;
    private final MobileDeviceRepository deviceRepository;
    private final AuthAsyncService authAsyncService;

    public LoginResponse login(@NotNull LoginRequest body) {
        var result = authValidator.validate(body);

        if (result.isNotValid()) {
            return LoginResponse.notOk(result.getMessage(), EErrorCode.UNAUTHORIZED);
        }

        switch (body.getGrant_type()) {
            case password:
                return loginWithPassword(body);
            default:
                return LoginResponse.notOk(messageSource
                        .getMessage("login_request.grant_type.unknown"), EErrorCode.BAD_REQUEST);
        }
    }

    @Transactional
    protected LoginResponse loginWithPassword(LoginRequest body) {
        try {
            var authentication = new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
            var authenticate = authenticationManager.authenticate(authentication);
            var user = (User) authenticate.getPrincipal();

            if (authValidator.isMobileUser(body.getClient_id())) {
                var new_secret = "";
                if (body.getClient_secret() == null) {
                    new_secret = "invalid-secret";
                } else {
                    new_secret = body.getClient_secret();
                }
                deviceRepository.findByDeviceId(new_secret).ifPresent(deviceRepository::delete);
                var device = new MobileDevice(user, new_secret);
                deviceRepository.save(device);
            }

            var accessToken = jwtUtil.generateAccessToken(user, body.getClient_id());

            return LoginResponse.ok(accessToken);
        } catch (AuthenticationException e) {
            return LoginResponse.notOk(e.getMessage(), EErrorCode.UNAUTHORIZED);
        }
    }


    public Response<RegisterDto> register(@NotNull RegisterRequest body) {
        var user = userFactory.createUser(body);

        var validationResult = authValidator.validate(user);

        if (validationResult.isNotValid()) {
            return Response.notOk(validationResult.getMessage(), EErrorCode.BAD_REQUEST);
        }

        user = userRepository.saveAndFlush(user);

        var locale = LocaleContextHolder.getLocale();

        eventPublisher.publishEvent(new RegistrationCompleteEvent(this, user, locale));

        return Response.ok(userFactory.createRegisterDto(user));
    }

    @Transactional
    public Response<ResendActivationCodeDto> resendActivationCode(ResendActivationCodeRequest body) {
        var locale = LocaleContextHolder.getLocale();

        var validationResult = authValidator.validate(body);

        if (validationResult.isNotValid()) {
            return Response.notOk(validationResult.getMessage(), EErrorCode.BAD_REQUEST);
        }

        userRepository.findByEmail(body.getEmail()).ifPresent(user -> {
            Hibernate.initialize(user.getVerificationCodes());

            eventPublisher.publishEvent(
                    new RegistrationCompleteEvent(this, user, locale)
            );
        });

        return Response.ok(
                new ResendActivationCodeDto(
                        messageSource.getMessage("resend_activation.success")
                )
        );
    }

    public Response<ForgotPasswordDto> forgotPassword(ForgotPasswordRequest body) {
        var locale = LocaleContextHolder.getLocale();

        var validationResult = authValidator.validate(body);

        if (validationResult.isValid()) {
            LoggableFuture.runAsync(() -> authAsyncService.forgotPasswordSuccess(body, locale));
        }

        return Response.ok(
                new ForgotPasswordDto(
                        messageSource.getMessage("forgot_password.success")
                )
        );
    }

    @Transactional
    public Response<ResetPasswordDto> resetPassword(ResetPasswordRequest body) {
        var validationResult = authValidator.validate(body);

        if (validationResult.isNotValid()) {
            return Response.notOk(validationResult.getMessage(), EErrorCode.BAD_REQUEST);
        }

        verificationCodeRepository.findByCode(body.getCode()).ifPresent(verificationCode -> {
            var user = verificationCode.getUser();

            user.setPassword(encoder.encode(body.getPassword()));
            verificationCodeRepository.delete(verificationCode);

            userRepository.save(user);
        });

        return Response.ok(new ResetPasswordDto(
                        messageSource.getMessage("reset_password.success")
                )
        );
    }

    @Transactional
    public Response<AccountActivationDto> activateAccount(AccountActivationRequest body) {
        var validationResult = authValidator.validate(body);

        if (validationResult.isNotValid()) {
            return Response.notOk(validationResult.getMessage(), EErrorCode.BAD_REQUEST);
        }

        var maybeVerificationCode = verificationCodeRepository.findByCode(body.getCode());

        maybeVerificationCode.ifPresent(verificationCode -> {
            var user = verificationCode.getUser();

            user.confirm();

            if (!ERole.isUser(user.getRole())) {
                user.setPassword(encoder.encode(body.getPassword()));
            }

            userRepository.save(user);
            verificationCodeRepository.delete(verificationCode);
        });

        return Response.ok(new AccountActivationDto(
                        messageSource.getMessage("activation.success")
                )
        );
    }

    @Transactional
    public Response<LogoutResponse> logout(String token, String deviceId) {
        var userId = jwtUtil.getUserId(token);

        var maybeDevice = deviceRepository.deleteByDeviceIdAndUserId(deviceId, userId);

        return maybeDevice.isEmpty() ?
                Response.notOk(messageSource.getMessage("logout.device_not_found"), EErrorCode.BAD_REQUEST) :
                Response.ok(new LogoutResponse(messageSource.getMessage("logout.success")));
    }

    @Transactional
    public Response<DeactivateResponse> deactivate(String token) {
        var userId = jwtUtil.getUserId(token);

        deviceRepository.deleteAllByUserId(userId);
        verificationCodeRepository.deleteAllByUserId(userId);

        var user = userRepository.getById(userId);

        user.deactivate();

        userRepository.save(user);

        return Response.ok(new DeactivateResponse(messageSource.getMessage("deactivate.success")));
    }
}
