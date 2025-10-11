package com.web.syncspace.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String BLACKLIST_PREFIX = "Blacklist:";

    public void blacklistToken(String accessToken, String refreshToken, long expirationMs, long refreshExpirationMs) {
        long accessTokenTTL = expirationMs - System.currentTimeMillis();
        long refreshTokenTTL = refreshExpirationMs - System.currentTimeMillis();
        if (accessTokenTTL > 0) {
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + accessToken, "Blacklisted", accessTokenTTL, TimeUnit.MILLISECONDS);
            if (refreshToken != null && refreshTokenTTL > 0) {
                redisTemplate.opsForValue().set(BLACKLIST_PREFIX + refreshToken, "Blacklisted", refreshTokenTTL, TimeUnit.MILLISECONDS);
            }
        }
    }

    public Boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }


}
