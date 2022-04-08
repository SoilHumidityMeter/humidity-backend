package com.soilhumidity.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class VerificationKeyConfig {
    @Value("#{T(java.lang.Integer).parseInt('${soilhm.verification_key.expiration.activation}')}")
    private int activationExpiration;

    @Value("#{T(java.lang.Integer).parseInt('${soilhm.verification_key.expiration.password_reset}')}")
    private int passwordReset;


    public int getActivationKeyExpirationTime() {
        return activationExpiration;
    }

    public int getPasswordResetKeyExpirationTime() {
        return passwordReset;
    }
}
