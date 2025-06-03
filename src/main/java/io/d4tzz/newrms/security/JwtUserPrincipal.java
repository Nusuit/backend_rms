package io.d4tzz.newrms.security;

import org.springframework.security.core.GrantedAuthority; // Import này cần thiết
import java.util.Collection; // Import này cần thiết

public class JwtUserPrincipal {
    private final Long id;
    private final Collection<? extends GrantedAuthority> authorities; // Thêm trường này

    public JwtUserPrincipal(Long id, Collection<? extends GrantedAuthority> authorities) { // Cập nhật constructor
        this.id = id;
        this.authorities = authorities; // Gán authorities
    }

    public long getIdentity() {
        return id;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() { // Thêm getter cho authorities
        return authorities;
    }
}

