package com.ssafy.backend.domain.member.controller;

import com.ssafy.backend.domain.member.dto.MemberLoginRequestDto;
import com.ssafy.backend.domain.member.dto.MemberLoginResponseDto;
import com.ssafy.backend.domain.member.dto.MemberSignUpRequestDto;
import com.ssafy.backend.domain.member.service.MemberService;
import com.ssafy.backend.global.common.dto.Message;
import com.ssafy.backend.global.component.jwt.dto.TokenDto;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import com.ssafy.backend.global.component.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<Message<Void>> signUpMember(@RequestBody MemberSignUpRequestDto signUpRequestDto) {
        memberService.signUpMember(signUpRequestDto);
        return ResponseEntity.ok().body(Message.success());
    }

    @PostMapping("/login")
    public ResponseEntity<Message<MemberLoginResponseDto>> login(@RequestBody MemberLoginRequestDto loginRequestDto) {
        TokenMemberInfoDto tokenMemberInfoDto = memberService.loginCheckMember(loginRequestDto);
        TokenDto tokenDto = jwtService.issueToken(tokenMemberInfoDto);
        MemberLoginResponseDto loginResponseDto = MemberLoginResponseDto.builder()
                .memberInfo(tokenMemberInfoDto)
                .token(tokenDto)
                .build();
        
        // JWT 토큰을 쿠키에 저장하는 로직 구현해야함

        return ResponseEntity.ok().body(Message.success(loginResponseDto));
    }
}
