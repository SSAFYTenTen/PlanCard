package com.ssafy.backend.domain.member.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberError {

    NOT_FOUND_USER(HttpStatus.INTERNAL_SERVER_ERROR, "해당 이메일을 가진 회원을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
