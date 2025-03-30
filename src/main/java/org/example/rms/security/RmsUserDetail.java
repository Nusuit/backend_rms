package org.example.rms.security;

public class RmsUserDetail {
    private final Long id;

    public RmsUserDetail(Long id) {
        this.id = id;
    }

    public long getIdentify() {
        return id;
    }
}
