package com.devteria.identityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED(9999, "Uncategorized error"),
    USER_EXISTS(1001, "User already exists"),
    USER_NOT_FOUND(1002, "User not found"),
    INVALID_CREDENTIALS(1003, "Invalid user name or password"),;

    private final int code;
    private final String message;
}
