package com.api.back.core.service;

import com.api.back.core.vo.AuthObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityManager {

    public Principal setContextAuthentication(AuthObject authUserObject) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                authUserObject,
                null,
                getAuthorities(authUserObject)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public List<SimpleGrantedAuthority> getAuthorities(AuthObject authUserObject) {
        return Collections.singletonList(new SimpleGrantedAuthority(authUserObject.getUserType().getStringStatus()));
    }
}