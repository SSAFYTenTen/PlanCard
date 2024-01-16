package com.ssafy.backend.global.component.jwt.service;

import com.ssafy.backend.global.component.jwt.dto.TokenDto;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import lombok.NonNull;

public interface JwtService {
    // 토큰 발급
    TokenDto issueToken(@NonNull TokenMemberInfoDto tokenMemberInfoDto);

    // access 토큰 파싱
    TokenMemberInfoDto parseAccessToken(@NonNull String accessToken);

    // Redis에 저장된 이메일을 통한 Access, Refresh 토큰 재발급
    TokenDto reissueToken(@NonNull String memberEmail);
}
