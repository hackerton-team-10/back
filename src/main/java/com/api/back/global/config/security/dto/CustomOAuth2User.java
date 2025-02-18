package com.api.back.global.config.security.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final MemberDto memberDto;

    public CustomOAuth2User(MemberDto memberDto) {

        this.memberDto = memberDto;
    }

    @Override
    public Map<String, Object> getAttributes() {

        Map<String, Object> response = new HashMap<>();
        response.put("memberData", memberDto);

        return response;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return memberDto.getRole();
            }
        });

        return collection;
    }

    public Long getUserName() {
        return memberDto.getUsername();
    }

    @Override
    public String getName() {
        return memberDto.getName();
    }

    public String getEmail() {return memberDto.getEmail();}

    public String getProfile() {return memberDto.getProfile();}

    public String getGoogleId() {return memberDto.getGoogleId();}

}