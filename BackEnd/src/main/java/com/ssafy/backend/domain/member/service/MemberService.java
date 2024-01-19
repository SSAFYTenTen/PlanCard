package com.ssafy.backend.domain.member.service;

import com.ssafy.backend.domain.member.dto.MemberLoginRequestDto;
import com.ssafy.backend.domain.member.dto.MemberPasswordUpdateDto;
import com.ssafy.backend.domain.member.dto.MemberSignUpRequestDto;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;

public interface MemberService {
    // 회원가입 기능
    void signUpMember(MemberSignUpRequestDto signUpRequestDto);

    // 로그인 기능
    TokenMemberInfoDto loginCheckMember(MemberLoginRequestDto loginRequestDto);

    // 로그아웃 기능
    void logoutMember(String email);

    // 비밀번호 변경 기능
    void updatePasswordMember(Long id, MemberPasswordUpdateDto passwordUpdateDto);
}
