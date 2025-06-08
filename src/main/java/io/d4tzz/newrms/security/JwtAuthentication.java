package io.d4tzz.newrms.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthentication extends AbstractAuthenticationToken {
    private final String token;
    private final JwtUserPrincipal userDetail;

    public JwtAuthentication(String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        userDetail = null;
    }

    public JwtAuthentication(String token, JwtUserPrincipal userDetail, Collection<? extends GrantedAuthority> authorities, boolean authenticated) {
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

    @Override
    public String getName() {
        if (userDetail != null) {
            return userDetail.getUsername();
        }
        return null; // Or throw an exception if a principal is always expected
    }
}
