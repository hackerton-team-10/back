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

@Profile("dev")
@Configuration
@OpenAPIDefinition(
    info = @Info(title = "blaybus API", version = "v1", description = "블레이버스 해커톤 개발 환경 API 문서"),
    security = @SecurityRequirement(name = "Authorization"),
    servers = {
        @Server(url="http://ec2-3-36-62-125.ap-northeast-2.compute.amazonaws.com:8080/api", description = "Dev Environment Swagger")
    }
)
@SecurityScheme(
    name = "Authorization",
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER
)
public class DevSwaggerConfig {


    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("spring")
            .pathsToMatch("/**")
            .build();
    }

}
