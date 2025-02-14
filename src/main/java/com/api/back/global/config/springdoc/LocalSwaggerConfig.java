package com.api.back.global.config.springdoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Security 관련 임시 주석
 * **/
@Profile("local")
@Configuration
@OpenAPIDefinition(
    info = @Info(title = "blaybus API", version = "v1", description = "블레이버스 해커톤 로컬 API 문서"),
//    security = @SecurityRequirement(name = "Authorization"),
    servers = {
        @Server(url="http://localhost:8080/api", description = "Local Swagger")
    }
)
//@SecurityScheme(name = "Authorization",
//    type = SecuritySchemeType.HTTP,
//    scheme = "bearer",
//    bearerFormat = "JWT",
//    in = SecuritySchemeIn.HEADER)
public class LocalSwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("spring")
            .pathsToMatch("/**")
            .build();
    }
}