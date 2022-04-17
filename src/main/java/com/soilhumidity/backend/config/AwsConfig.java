package com.soilhumidity.backend.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;

import javax.annotation.PostConstruct;
import java.net.URI;

@Configuration
public class AwsConfig {
    @Value("#{'${spring.profiles.active}'.split('-')[0]}")
    private String profile;

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    @Getter
    @Value("${soilhm.aws.s3.bucket-name}")
    private String bucketName;

    @Getter
    @Value("${soilhm.aws.s3.region}")
    private String region;

    @Getter
    @Value("${soilhm.aws.s3.base-url}")
    private String baseUrl;

    @Getter
    private AwsBasicCredentials credentials = null;

    @Getter
    private URI endpoint = null;

    @PostConstruct()
    protected void init() {
        if (!profile.equals("dev") && !profile.equals("prod")) {
            throw new IllegalStateException("Profile must be start with 'dev' or 'prod'.");
        }
        endpoint = URI.create("https://" + region + "." + baseUrl);
        credentials = AwsBasicCredentials.create(accessKey, secretKey);
    }

}
