package com.web.syncspace.security.jwt;


import com.web.syncspace.security.userdetaiils.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    @Value("${spring.jwt.accessTokenExpirationMs}")
    private long accessTokenExpirationMs;

    @Value("${spring.jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    @Value("${spring.jwt.clockSkewMs}")
    private long clockSkewMs;


    @Contract(" -> new")
    private @NotNull SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private String buildToken(Map<String, Object> Claims, String subject, long expirationMs) {
        return Jwts.builder()
                .subject(subject)
                .claims(Claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key())
                .compact();
    }

    public String generateAccessToken(CustomUserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return buildToken(claims, customUserDetails.getUsername(), accessTokenExpirationMs);
    }

    public String generateRefreshToken(CustomUserDetails customUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        return buildToken(claims, customUserDetails.getUsername(), refreshTokenExpirationMs);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .clockSkewSeconds(clockSkewMs / 1000L)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractAccessTokenExpirationDate(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public Date extractRefreshTokenExpirationDate(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRole(String token) {
        return (List<String>) extractClaims(token, claims -> claims.get("role", List.class));
    }

    public Boolean isTokenExpired(String token) {
        return extractAccessTokenExpirationDate(token).before(new Date());
    }

    public Boolean isTokenValid(String token, CustomUserDetails customUserDetails) {
        final String username = extractUsername(token);
        return (username.equals(customUserDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenStructureValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
