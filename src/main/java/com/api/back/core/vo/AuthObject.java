package com.api.back.core.vo;

import com.api.back.core.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AuthObject {

    @Schema(description = "User ID")
    private String userId;

    @Schema(description = "User type (CUSTOMER, ADMIN, UNKNOWN)")
    private UserType userType;

    private AuthObject(UserType userType) {
        this.userType = userType;
    }

    public static AuthObject getUnknownAuthObject() {
        return new AuthObject(UserType.UNKNOWN);
    }

    public AuthObject(String userId, UserType userType) {
        this.userId = userId;
        this.userType = userType != null ? userType : UserType.UNKNOWN;
    }

    public String getAndValidCustomerId() {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userId;
    }
}