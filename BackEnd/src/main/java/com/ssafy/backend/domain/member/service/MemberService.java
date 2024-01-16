package com.ssafy.backend.domain.member.service;

import com.ssafy.backend.domain.member.dto.MemberSignUpRequestDto;

public interface MemberService {
    // 회원가입 기능
    void signUpMember(MemberSignUpRequestDto signUpRequestDto);

}
