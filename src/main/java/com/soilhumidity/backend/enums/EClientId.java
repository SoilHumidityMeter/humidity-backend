package com.soilhumidity.backend.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@NoArgsConstructor
@Getter
public class EClientId {
    @Value("${soilhm.jwt.client_id.mobile}")
    private String mobileClientId;

    @Value("${soilhm.jwt.client_id.web}")
    private String webClientId;
    
}
