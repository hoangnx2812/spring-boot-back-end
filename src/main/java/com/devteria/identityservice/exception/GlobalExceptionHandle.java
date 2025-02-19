package com.devteria.identityservice.exception;

import com.devteria.identityservice.dto.response.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
public class GlobalExceptionHandle {


    //Bắt lỗi khi validate dữ liệu

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(400)
                .message(ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage())
                .build());
    }

    //Bắt lỗi đã quy ước
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(ApiResponse.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getErrorCode().getMessage())
                .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }


    //    Bắt lỗi chưa xác định
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED.getCode())
                .message(ErrorCode.UNCATEGORIZED.getMessage())
                .build());
    }
}
