package com.soilhumidity.backend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${soilhm.url}")
    private String url;

    @Value("${soilhm.tmp-folder}")
    private String temporaryFolder;
}
