package org.example.rms.security;

public class UserPrincipal {
    private final Long id;

    public UserPrincipal(Long id) {
        this.id = id;
    }

    public long getIdentity() {
        return id;
    }
}
