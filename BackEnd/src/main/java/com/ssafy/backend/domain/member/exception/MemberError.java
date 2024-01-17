package com.ssafy.backend.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberError {

    NOT_FOUND_MEMBER(HttpStatus.INTERNAL_SERVER_ERROR, "해당 이메일을 가진 회원을 찾을 수 없습니다."),
    EXIST_MEMBER_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "이미 가입되어 있는 이메일입니다."),
    ALREADY_MEMBER_LOGOUT(HttpStatus.BAD_REQUEST, "이미 로그아웃 된 회원입니다.");


    private final HttpStatus httpStatus;
    private final String errorMessage;
}
