package com.devteria.identityservice.exception;

import com.devteria.identityservice.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;


@Slf4j
@ControllerAdvice

public class GlobalExceptionHandle {

    private static final String MIN_ATTRIBUTE = "min";

    //Bắt lỗi khi validate dữ liệu
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation =
                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
            log.info(attributes.toString());

        } catch (IllegalArgumentException e) {
            log.error("Invalid error code: {}", enumKey);
        }
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

    //Bắt lỗi đã quy ước
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(ApiResponse.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getErrorCode().getMessage())
                .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException() {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }

    //Bắt lỗi chưa xác định
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
        log.error("An error occurred", ex);
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED.getCode())
                .message(ErrorCode.UNCATEGORIZED.getMessage())
                .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpRequestMethodNotSupportedException() {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build());
    }
}

