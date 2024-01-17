package com.ssafy.backend.global.exception;

import lombok.Getter;

@Getter
public class TokenException extends RuntimeException {
    private final GlobalError errorCode;
    private final int status;
    private final String errorMessage;

    public TokenException(GlobalError errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.status = errorCode.getHttpStatus().value();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public TokenException(GlobalError errorCode, Throwable e) {
        super(errorCode.getErrorMessage(), e);
        this.errorCode = errorCode;
        this.status = errorCode.getHttpStatus().value();
        this.errorMessage = errorCode.getErrorMessage();
    }
}
