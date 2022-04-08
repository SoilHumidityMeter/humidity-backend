package com.soilhumidity.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MailConfig {

    @Getter
    @Value("#{'${spring.mail.username}'}")
    private String from;
}
