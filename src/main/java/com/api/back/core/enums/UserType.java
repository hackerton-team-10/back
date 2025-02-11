package com.api.back.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum UserType {
    ADMIN("admin", "/api/admin"),
    CUSTOMER("customer", "/api/customer"),
    UNKNOWN("open", "/api/open");

    private final String stringStatus;
    private final String requestMapper;

    UserType(String stringStatus, String requestMapper) {
        this.stringStatus = stringStatus;
        this.requestMapper = requestMapper;
    }

}