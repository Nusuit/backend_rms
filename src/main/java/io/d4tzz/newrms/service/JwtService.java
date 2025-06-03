package io.d4tzz.newrms.service;

import io.d4tzz.newrms.exception.InvalidJsonWebTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${rms.jwt.access.token.key}")
    private String ACCESS_TOKEN_KEY;

    @Value("${rms.jwt.access.token.expiration}")
    private Long ACCESS_TOKEN_EXPIRATION_SECONDS;

    @Value("${rms.jwt.refresh.token.key}")
    private String REFRESH_TOKEN_KEY;

    @Value("${rms.jwt.refresh.token.expiration}")
    private Long REFRESH_TOKEN_EXPIRATION_SECONDS;

    private SecretKey ACCESS_TOKEN_SECRET_KEY;
    private SecretKey REFRESH_TOKEN_SECRET_KEY;

    @PostConstruct
    public void init() {
        this.ACCESS_TOKEN_SECRET_KEY = new SecretKeySpec(ACCESS_TOKEN_KEY.getBytes(), "HmacSHA256");
        this.REFRESH_TOKEN_SECRET_KEY = new SecretKeySpec(REFRESH_TOKEN_KEY.getBytes(), "HmacSHA256");
    }


    public String generateToken(String subject, long expirationTime, Map<String, String> claims, SecretKey key) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime * 1000);

        return Jwts.builder()
                .header()
                .type("jwt")
                .and()
                .subject(subject)
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();

    }

    public JwtBuilder accessToken() {
        return new JwtBuilder(this.ACCESS_TOKEN_SECRET_KEY, ACCESS_TOKEN_EXPIRATION_SECONDS, false);
    }

    public JwtBuilder refreshToken() {
        return new JwtBuilder(this.REFRESH_TOKEN_SECRET_KEY, REFRESH_TOKEN_EXPIRATION_SECONDS, true);
    }

    public class JwtBuilder {
        private String subject;
        private long expirationTime;
        private Map<String, String> claims;
        private SecretKey key;


        public JwtBuilder(SecretKey key, long expirationTime, boolean isRefreshToken) {
            claims = new HashMap<>();
            this.key = key;
            this.expirationTime = expirationTime;

            if (isRefreshToken) {
                claims.put("token-type", "refresh");
            } else {
                claims.put("token-type", "access");
            }
        }

        public JwtBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public JwtBuilder claim(String key, String value) {
            claims.put(key, value);
            return this;
        }

        public JwtBuilder email(String email) { // Thêm phương thức để thêm email claim
            claims.put("email", email);
            return this;
        }

        public JwtBuilder role(String role) {
            claims.put("role", role);
            return this;
        }

        public JwtBuilder claims(Map<String, String> claims) {
            claims.putAll(this.claims);
            return this;
        }

        public String jwt() {
            return generateToken(subject, expirationTime, claims, key);
        }
    }

    public Map<String, String> validateToken(String token, SecretKey key) {
        Jwt<?, ?> jwt;
        try {
            jwt = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parse(token);
        } catch (JwtException e) {
            throw new InvalidJsonWebTokenException("Token is invalid");
        }

        Claims claims = (Claims) jwt.getPayload();

        Map<String, String> claimsMap = new HashMap<>(claims.size());

        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            claimsMap.put(entry.getKey(), entry.getValue().toString());
        }

        return claimsMap;
    }

    public Map<String, String> validateAccessToken(String token) {
        Map<String, String> claimsMap = validateToken(token, ACCESS_TOKEN_SECRET_KEY);

        String isRefreshToken = claimsMap.get("token-type");
        if (isRefreshToken == null || !isRefreshToken.equals("access")) {
            throw new InvalidJsonWebTokenException("Access token is invalid");
        }

        return claimsMap;
    }

    public Map<String, String> validateRefreshToken(String token) {
        Map<String, String> claimsMap = validateToken(token, REFRESH_TOKEN_SECRET_KEY);

        String isRefreshToken = claimsMap.get("token-type");
        if (isRefreshToken == null || !isRefreshToken.equals("refresh")) {
            throw new InvalidJsonWebTokenException("Refresh token is invalid");
        }

        return claimsMap;
    }
}
