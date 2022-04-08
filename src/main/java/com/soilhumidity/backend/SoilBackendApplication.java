package com.soilhumidity.backend;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableSwagger2
@EnableScheduling
@SpringBootApplication()
public class SoilBackendApplication {

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(SoilBackendApplication.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}
