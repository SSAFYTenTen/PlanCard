package com.ssafy.backend.domain.member.dto;

import com.ssafy.backend.global.component.jwt.dto.TokenDto;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class MemberLoginResponseDto {
    private TokenMemberInfoDto memberInfo;
    private TokenDto token;
}
