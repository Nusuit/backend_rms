package org.example.rms.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthentication extends AbstractAuthenticationToken {
    private final String token;
    private final UserPrincipal userDetail;

    public JwtAuthentication(String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        userDetail = null;
    }

    public JwtAuthentication(String token, UserPrincipal userDetail, Collection<? extends GrantedAuthority> authorities, boolean authenticated) {
        super(authorities);
        this.token = token;
        this.userDetail = userDetail;
        super.setAuthenticated(authenticated);
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetail;
    }
}
