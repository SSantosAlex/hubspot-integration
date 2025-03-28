package com.hubspot.exception;

import lombok.Getter;

@Getter
public class OAuthException extends RuntimeException {
    private final String errorCode;
    private final String provider;
    private final int httpStatus;

    public OAuthException(String errorCode, String message, String provider, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.provider = provider;
        this.httpStatus = httpStatus;
    }
}
