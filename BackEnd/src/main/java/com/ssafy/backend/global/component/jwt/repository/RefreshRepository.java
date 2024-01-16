package com.ssafy.backend.global.component.jwt.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class RefreshRepository {
    private final RedisTemplate<String, String> redisTemplate;

    private static final String KEY_PREFIX = "refreshToken::";

    public void save(String userEmail, String token, int expiresMin) {
        String key = KEY_PREFIX + userEmail;
        redisTemplate.opsForValue().set(key, token, Duration.ofMinutes(expiresMin));
        redisTemplate.expire(key, expiresMin, TimeUnit.MINUTES);    // 만료시간 설정
    }

    public Optional<String> find(String memberEmail) {
        String token = redisTemplate.opsForValue().get(KEY_PREFIX + memberEmail);
        return Optional.ofNullable(token);
    }

    public void delete(String memberEmail) {
        redisTemplate.delete(KEY_PREFIX + memberEmail);
    }
}
