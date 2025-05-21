package io.d4tzz.newrms.security;

public class JwtUserPrincipal {
    private final Long id;

    public JwtUserPrincipal(Long id) {
        this.id = id;
    }

    public long getIdentity() {
        return id;
    }

}
