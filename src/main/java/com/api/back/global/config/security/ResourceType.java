package com.api.back.global.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

@Getter
@RequiredArgsConstructor
public enum ResourceType {
    StaticResources(
            Arrays.asList(
                    "/css/", "/js/",
                    "/swagger-ui/", "/swagger-resources/", "/v3/",
                    "/h2-console/", "/login/", "/here-chat/"
            ),
            Arrays.asList(".ico", "/swagger-config"),
            false, // 토큰 불필요 리소스
            false
    ),

    /**
     * 토큰 없이 호출 가능
     */
    OpenResource(
            Collections.singletonList("/api/v1/open"),
            emptyList(),
            false, // 토큰 불필요 리소스
            true
    ),

    ApiResources(
            Collections.singletonList("/api/v1"),
            emptyList(),
            true, // 토큰 필요 리소스
            true
    ),

    UNKNOWN(emptyList(),
            emptyList(),
            false,
            false
    );

    private final List<String> startWith;
    private final List<String> endWith;
    private final boolean needToken;
    private final boolean needAuthReq;


    public static ResourceType getResourceType(String requestUri) {
        return Arrays.stream(ResourceType.values())
                .filter(type ->
                        type.startWith.stream().anyMatch(requestUri::startsWith) ||
                                type.endWith.stream().anyMatch(requestUri::endsWith)
                )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant ResourceType for string: " + requestUri));
    }
}