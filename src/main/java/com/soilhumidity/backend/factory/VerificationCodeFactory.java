package com.soilhumidity.backend.factory;

import com.soilhumidity.backend.enums.EVerificationKeyType;
import com.soilhumidity.backend.util.RandomUtil;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import com.soilhumidity.backend.config.VerificationKeyConfig;
import com.soilhumidity.backend.model.User;
import com.soilhumidity.backend.model.VerificationCode;

import java.time.Instant;
import java.util.Date;

@Component
@AllArgsConstructor
public class VerificationCodeFactory {
    private final VerificationKeyConfig verificationKeyConfig;

    public VerificationCode createActivationCode(@NotNull User user) {
        return new VerificationCode(
                RandomUtil.generate(),
                user,
                getExpirationDate(verificationKeyConfig.getActivationKeyExpirationTime()),
                EVerificationKeyType.ACTIVATION
        );
    }

    public VerificationCode createPasswordResetCode(@NotNull User user) {
        return new VerificationCode(
                RandomUtil.generate(),
                user,
                getExpirationDate(verificationKeyConfig.getPasswordResetKeyExpirationTime()),
                EVerificationKeyType.PASSWORD_RESET
        );
    }

    private Date getExpirationDate(int nextSeconds) {
        return Date.from(Instant.now().plusSeconds(nextSeconds));
    }
}
