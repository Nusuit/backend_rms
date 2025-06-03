package io.d4tzz.newrms.security;

import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class JwtUserPrincipal {
    private final Long id;
    private final String email; // Thêm trường email
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUserPrincipal(Long id, String email, Collection<? extends GrantedAuthority> authorities) { // Cập nhật constructor
        this.id = id;
        this.email = email; // Gán email
        this.authorities = authorities;
    }

    public long getIdentity() {
        return id;
    }

    public String getEmail() { // Thêm getter cho email
        return email;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
