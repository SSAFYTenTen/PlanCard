package com.ssafy.backend.domain.member.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {
    private final MemberError errorCode;
    private final int status;
    private final String errorMessage;

    public MemberException(MemberError errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.status = errorCode.getHttpStatus().value();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public MemberException(MemberError errorCode, Throwable e, String errorMessage) {
        super(errorCode.getErrorMessage(), e);
        this.errorCode = errorCode;
        this.status = errorCode.getHttpStatus().value();
        this.errorMessage = errorMessage;
    }

}
