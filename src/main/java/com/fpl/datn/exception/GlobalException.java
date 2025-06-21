package com.fpl.datn.exception;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalException {
    private static final String MIN_ATTRIBUTES = "min";
    UserMapper userMapper;

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> handException(Exception exception) {
        ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;
        ApiResponse<Void> apiResponse = new ApiResponse<Void>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<Void>> handRuntimeException(RuntimeException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        ApiResponse<Void> apiResponse = new ApiResponse<Void>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Void>> handlRuntimeException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<Void> apiResponse = new ApiResponse<Void>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> hanlMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        if (enumKey.isEmpty()) {
            throw new NullPointerException();
        }

        ErrorCode errorCode = ErrorCode.INVALID_MESSAGE_KEY;
        Map<String, Object> attributes = null;
        try {
            errorCode = ErrorCode.valueOf(enumKey);

            var constrainViolation =
                    exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            attributes = constrainViolation.getConstraintDescriptor().getAttributes();

            log.info(attributes.toString());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }

        ApiResponse<Void> apiResponse = new ApiResponse<Void>();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(
                Objects.nonNull(attributes)
                        ? mapAttribute(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValues = String.valueOf(attributes.get(MIN_ATTRIBUTES));
        return message.replace("{" + MIN_ATTRIBUTES + "}", minValues);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse<Void>> responseEntity(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.<Void>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
