package org.example.rms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import org.example.rms.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthentication auth = (JwtAuthentication) authentication;

        Claims claims = null;
        try {
            Jwt<?, ?> jwt = JwtUtils.validateAccessToken(auth.getToken());
            claims = (Claims) jwt.getPayload();
        } catch (JwtException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }

        String role = (String) claims.get("role");
        String id = claims.getSubject();

        Collection<SimpleGrantedAuthority> authorities = null;
        if (role != null) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        }

        return new JwtAuthentication(auth.getToken(), new RmsUserDetail(Long.parseLong(id)), authorities, true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.isAssignableFrom(authentication);
    }
}
