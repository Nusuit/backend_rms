package org.example.rms.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.example.rms.entity.User;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final SecretKey ACCESS_TOKEN_KEY = new SecretKeySpec(getAccessTokenKey().getBytes(), "HmacSHA256");
    private static final SecretKey REFRESH_TOKEN_KEY = new SecretKeySpec(getRefreshTokenKey().getBytes(), "HmacSHA256");

    private static final int ACCESS_TOKEN_DURATION_IN_HOUR = 5;
    private static final int REFRESH_TOKEN_DURATION_IN_HOUR = 24;


    public static String generateToken(User user, Map<String, String> claims, int durationInHour, SecretKey key) {
        String userId = String.valueOf(user.getId());

        LocalDateTime now = LocalDateTime.now();
        Date issuedAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime expirationTime = now.plusSeconds(durationInHour * 3600L);
        Date expiration = Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .header().type("jwt")
                .and()
                .subject(userId)
                .claim("role", user.getRole().getRoleName())
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public static String generateRefreshToken(User user) {
        Map<String, String> claims = new HashMap<>();
        claims.put("token_type", "refresh");
        return generateToken(user, claims, REFRESH_TOKEN_DURATION_IN_HOUR, ACCESS_TOKEN_KEY);
    }

    public static String generateAccessToken(User user) {
        return generateToken(user, null, ACCESS_TOKEN_DURATION_IN_HOUR, REFRESH_TOKEN_KEY);
    }

    private static String getAccessTokenKey() {
        return "thisIsASecretKeyWithAtLeast32CharactersLong!";
    }

    private static String getRefreshTokenKey() {
        return "thisIsASecretKeyWithAtLeast32CharactersLong!";
    }


    public static Jwt<?, ?> validateToken(String token, SecretKey key) throws JwtException {
        Jwt<?, ?> jwt;
        jwt = Jwts.parser()
                .verifyWith(key)
                .build()
                .parse(token);
        return jwt;
    }

    public static Jwt<?, ?> validateAccessToken(String token) throws JwtException {
        return validateToken(token, ACCESS_TOKEN_KEY);
    }

    public static Jwt<?, ?> validateRefreshToken(String token) throws JwtException {
        Jwt<?, ?> jwt =  validateToken(token, REFRESH_TOKEN_KEY);
        Claims claims = (Claims) jwt.getPayload();
        String tokenType = (String) claims.get("token_type");
        if(tokenType == null || !tokenType.equals("refresh")) {
            throw new JwtException("Invalid refresh token");
        }
        return jwt;
    }

    public static String getClaimValue(Claims claims, String claimKey) {
        return (String) claims.get(claimKey);
    }

    public static String getRole(Claims claims, String claimKey) {
        return getClaimValue(claims, claimKey);
    }

}
