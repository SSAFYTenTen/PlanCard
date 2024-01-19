package com.ssafy.backend.domain.member.controller;

import com.ssafy.backend.domain.member.dto.*;
import com.ssafy.backend.domain.member.service.MemberService;
import com.ssafy.backend.global.common.dto.Message;
import com.ssafy.backend.global.component.jwt.dto.TokenDto;
import com.ssafy.backend.global.component.jwt.dto.TokenMemberInfoDto;
import com.ssafy.backend.global.component.jwt.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Message<MemberLoginResponseDto>> login(@RequestBody MemberLoginRequestDto loginRequestDto,
                                                                 HttpServletResponse response) {
        TokenMemberInfoDto tokenMemberInfoDto = memberService.loginCheckMember(loginRequestDto);
        TokenDto tokenDto = jwtService.issueToken(tokenMemberInfoDto);
        MemberLoginResponseDto loginResponseDto = MemberLoginResponseDto.builder()
                .memberInfo(tokenMemberInfoDto)
                .token(tokenDto)
                .build();
        // JWT 토큰을 쿠키에 저장
        Cookie accessTokenCookie = new Cookie("accessToken", tokenDto.getAccessToken());
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(3600); // 60분(3600초)으로 설정
        response.addCookie(accessTokenCookie);

        return ResponseEntity.ok().body(Message.success(loginResponseDto));
    }

    @GetMapping("/logout")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> logoutMember(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto,
                                                      HttpServletResponse response) {
        memberService.logoutMember(loginActiveDto.getEmail());

        // 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        return ResponseEntity.ok().body(Message.success());
    }

    @PutMapping("/update/password")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Message<Void>> updatePasswordMember(@AuthenticationPrincipal MemberLoginActiveDto loginActiveDto,
                                                              @RequestBody MemberPasswordUpdateDto passwordUpdateDto) {
        memberService.updatePasswordMember(loginActiveDto.getId(), passwordUpdateDto);
        return ResponseEntity.ok().body(Message.success());
    }
}
