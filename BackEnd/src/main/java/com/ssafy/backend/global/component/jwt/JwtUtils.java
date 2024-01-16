package com.ssafy.backend.global.component.jwt;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Base64;
import io.jsonwebtoken.security.Keys;

@Getter
@Component
public class JwtUtils {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAME = "name";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_ROLE = "role";

    @Value("${jwt.secret-key.access}")
    private String accessKey;

    @Value("${jwt.secret-key.refresh}")
    private String refreshKey;

    @Value("${jwt.expired-min.access}")
    private int accessTokenExpiredMin;

    @Value("${jwt.expired-min.refresh}")
    private int refreshTokenExpiredMin;

    private Key encodedAccessKey;
    private Key encodedRefreshKey;

    // 로직 탈 때 수행되는 메서드
    @PostConstruct
    private void init() {
        encodedAccessKey = Keys.hmacShaKeyFor(
                Base64.getEncoder().encodeToString(accessKey.getBytes()).getBytes());

        encodedRefreshKey = Keys.hmacShaKeyFor(
                Base64.getEncoder().encodeToString(refreshKey.getBytes()).getBytes());
    }
}
