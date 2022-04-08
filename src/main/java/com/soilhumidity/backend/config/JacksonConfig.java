package com.soilhumidity.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.soilhumidity.backend.util.mapper.PointMixin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.Point;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper jacksonObjectMapper() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(propertyNamingStrategy())
                .addMixIn(Point.class, PointMixin.class);
    }

    @Bean
    public PropertyNamingStrategy propertyNamingStrategy() {
        return new PropertyNamingStrategies.SnakeCaseStrategy();
    }
}
