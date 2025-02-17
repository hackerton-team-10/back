package com.api.back.global.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        corsRegistry.addMapping("/**")
            .allowedOrigins("http://localhost:8080", "http://localhost:3000")  // 여러 포트 허용
            .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(), HttpMethod.HEAD.name(), HttpMethod.OPTIONS.name(),
                HttpMethod.PATCH.name())
            .allowedHeaders("Authorization", "Content-Type")
            .exposedHeaders("Set-Cookie", "Authorization")
            .allowCredentials(true); // 쿠키 인증 요청 허용
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("http://localhost:8080")); // 클라이언트 URL
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
