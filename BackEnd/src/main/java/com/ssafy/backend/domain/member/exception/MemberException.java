package com.ssafy.backend.domain.member.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private final MemberError errorCode;
    private final int status;
    private final String errorMessage;

    public MemberException(MemberError errorCode, MemberError errorCode1, int status, String errorMessage) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode1;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public MemberException(MemberError errorCode, Throwable e, String errorMessage) {
        super(errorCode.getErrorMessage(), e);
        this.errorCode = errorCode;
        this.status = errorCode.getHttpStatus().value();
        this.errorMessage = errorMessage;
    }
}
