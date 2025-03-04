package com.api.back.global.config.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private String role;
    private String name;
    private Long username;
    private String googleId;
    private String email;
    private String profile;
}
