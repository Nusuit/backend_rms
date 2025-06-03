package io.d4tzz.newrms.security;

import io.d4tzz.newrms.exception.InvalidJsonWebTokenException;
import io.d4tzz.newrms.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthentication auth = (JwtAuthentication) authentication;

        Map<String, String> claims;
        try {
            claims = jwtService.validateAccessToken(auth.getToken());
        } catch (InvalidJsonWebTokenException e) {
            System.out.println("Loi o day");
            throw new AuthenticationServiceException(e.getMessage());
        }

        String role = claims.get("role");
        String id = claims.get("sub");
        String email = claims.get("email"); // Lấy email từ claims

        Collection<SimpleGrantedAuthority> authorities = null;
        if (role != null) {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        }

        // SỬA ĐỔI: Truyền email vào constructor của JwtUserPrincipal
        return new JwtAuthentication(auth.getToken(), new JwtUserPrincipal(Long.parseLong(id), email, authorities), authorities, true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.isAssignableFrom(authentication);
    }
}
