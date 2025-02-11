package com.api.back.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeaderPrefix {
    AUTHORIZATION("Authorization"),
    REFRESHTOKEN("RefreshToken");

    private final String prefix;

}