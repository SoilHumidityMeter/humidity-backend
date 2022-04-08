package com.soilhumidity.backend.config;

import com.fasterxml.classmate.TypeResolver;
import com.soilhumidity.backend.util.annotations.ApiInformation;
import com.soilhumidity.backend.util.status.AdditionalModel;
import com.soilhumidity.backend.util.status.EnumUtil;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer, WebMvcOpenApiTransformationFilter {
    private static final String SCHEMA_NAME = "Credentials";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${soilhm.version}")
    private String version;

    @Value("${springfox.documentation.swaggerUi.baseUrl}")
    private String swaggerBaseUrl;

    @Value("${soilhm.url}")
    private String url;

    @NotNull
    private List<SecurityScheme> securitySchema() {
        String tokenUrl = "/auth/login";

        return Collections.singletonList(
                OAuth2Scheme.OAUTH2_PASSWORD_FLOW_BUILDER
                        .name(SCHEMA_NAME)
                        .tokenUrl(tokenUrl)
                        .scopes(List.of(new AuthorizationScope("Default", "Default scope.")))
                        .build()
        );
    }

    private SecurityContext securityContext() {
        return SecurityContext
                .builder()
                .operationSelector(
                        o -> PathSelectors
                                .ant("/auth/*").and(path -> !path.contains("logout") && !path.contains("deactivate"))
                                .negate()
                                .test(o.requestMappingPattern())
                )
                .securityReferences(Collections.singletonList(new SecurityReference(SCHEMA_NAME, new AuthorizationScope[0])))
                .build();
    }

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(getApiInfo())
                .additionalModels(typeResolver.resolve(AdditionalModel.MODEL))
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.POST, EnumUtil.globalResponses())
                .globalResponses(HttpMethod.GET, EnumUtil.globalResponses())
                .globalResponses(HttpMethod.PUT, EnumUtil.globalResponses())
                .globalResponses(HttpMethod.PATCH, EnumUtil.globalResponses())
                .globalResponses(HttpMethod.DELETE, EnumUtil.globalResponses())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(securitySchema())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("SoilHumidityMeter Backend")
                .description("SoilHumidityMeter Backend Documentation")
                .version(version)
                .build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        var swaggerUiUrl = swaggerBaseUrl + "/swagger-ui/";

        registry.addRedirectViewController(swaggerBaseUrl, swaggerUiUrl)
                .setKeepQueryParams(true);
        registry.addRedirectViewController(swaggerBaseUrl + "/", swaggerUiUrl)
                .setKeepQueryParams(true);
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return delimiter.equals(DocumentationType.OAS_30);
    }

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        var swagger = context.getSpecification();

        swagger.setServers(getServers());
        swagger.setTags(getTags());

        return swagger;
    }

    public List<Server> getServers() {
        var server = new Server();

        server.setUrl(url);

        return List.of(server);
    }

    public List<Tag> getTags() {
        var tags = new LinkedList<Tag>();

        var scanner = new AnnotatedTypeScanner(ApiInformation.class);

        for (var controllerClass : scanner.findTypes("com.soilhumidity.backend.controller")) {
            try {

                var information = controllerClass.getAnnotation(ApiInformation.class);

                tags.add(new Tag().name(information.tag()).description(information.description()));
            } catch (Exception e) {
                logger.warn("The controller class {} does not have ApiInformation annotation. " +
                        "All rest controllers must have these.", controllerClass.getSimpleName());
            }
        }

        tags.sort(Comparator.comparing(Tag::getName));

        return tags;
    }
}