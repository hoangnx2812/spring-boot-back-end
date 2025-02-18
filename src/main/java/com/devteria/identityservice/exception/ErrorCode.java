package com.devteria.identityservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_EXISTS(1001, "User already exists"),
    UNCATEGORIZED(9999, "Uncategorized error");

    private final int code;
    private final String message;
}
