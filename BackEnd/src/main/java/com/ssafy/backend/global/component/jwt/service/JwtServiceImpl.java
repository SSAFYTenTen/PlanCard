package com.ssafy.backend.global.component.jwt.service;

import com.ssafy.backend.global.component.jwt.JwtIssuer;
import com.ssafy.backend.global.component.jwt.JwtParser;
import com.ssafy.backend.global.component.jwt.JwtUtils;
import com.ssafy.backend.global.component.jwt.dto.TokenDto;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import com.ssafy.backend.global.component.jwt.repository.RefreshRepository;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ssafy.backend.global.component.jwt.JwtUtils.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final JwtUtils jwtUtils;
    private final JwtIssuer jwtIssuer;
    private final JwtParser jwtParser;
    private final RefreshRepository refreshRepository;


    @Override
    public TokenDto issueToken(@NonNull TokenMemberInfoDto tokenMemberInfoDto) {
        String accessToken = jwtIssuer.issueToken(
                tokenMemberInfoDto.toClaims(jwtUtils.getAccessTokenExpiredMin()), jwtUtils.getEncodedAccessKey()
        );

        String refreshToken = jwtIssuer.issueToken(
                tokenMemberInfoDto.toClaims(jwtUtils.getRefreshTokenExpiredMin()), jwtUtils.getEncodedRefreshKey()
        );

        try {
            // refreshToekn redis에 저장
            refreshRepository.save(tokenMemberInfoDto.getEmail(), refreshToken, jwtUtils.getRefreshTokenExpiredMin());
        }
        catch (Exception e) {
            throw new RuntimeException("Redis 연결에 실패했습니다.");
        }

        return TokenDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(jwtUtils.getAccessTokenExpiredMin())
                .grantType(BEARER_PREFIX)
                .build();
    }

    @Override
    public TokenMemberInfoDto parseAccessToken(@NonNull String accessToken) {
        Claims claims = jwtParser.parseToken(accessToken, jwtUtils.getEncodedAccessKey());
        if (claims == null) {
            return null;
        }
        return TokenMemberInfoDto.from(claims);
    }

    @Override
    public TokenDto reissueToken(@NonNull String memberEmail) {
        return null;
    }
}
