package com.api.back.global.config.security;

import com.api.back.global.config.security.handler.CustomSuccessHandler;
import com.api.back.global.config.security.jwt.JWTFilter;
import com.api.back.global.config.security.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, JWTUtil jwtUtil) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //Swagger, /v1/** Filter 제외
        return web -> {
            web.ignoring()
                .requestMatchers(
                    "/h2-console/**",
                    "favicon.ico",
                    "/error",
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/api-docs/**",
                    "/v1/**"
                );
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                    CorsConfiguration configuration = new CorsConfiguration();

                    // 허용할 Origin 추가 (Swagger UI 및 클라이언트 주소)
                    configuration.setAllowedOrigins(Arrays.asList(
                        "http://localhost:5173", // 클라이언트 주소
                        "http://hair-fe-smoky.vercel.app",
                        "http://localhost:8080",   // Swagger UI 주소
                        "http://ec2-3-36-62-125.ap-northeast-2.compute.amazonaws.com:8080"  //EC2 도메인
                    ));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                }
            }));


        //csrf disable
        http
            .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
            .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
            .httpBasic((auth) -> auth.disable());


        //JWTFilter 추가
        http
            .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

        //oauth2
        http
            .oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                    .userService(customOAuth2UserService))
                .successHandler(customSuccessHandler));

        //경로별 인가
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/v1/**", "/payment/callback", "/payment/callback/test").permitAll()
                .anyRequest().authenticated());

        //세션 설정 : STATELESS
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
