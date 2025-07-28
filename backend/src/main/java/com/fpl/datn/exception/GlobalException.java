package com.fpl.datn.exception;

import java.nio.file.AccessDeniedException;
import java.util.Map;

import jakarta.validation.ConstraintViolation;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fpl.datn.dto.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalException {
    private static final String MIN_ATTRIBUTES = "min";
    private static final String MAX_ATTRIBUTES = "max";

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
        String enumKey = null;
        String finalMessage = "Dữ liệu đầu vào không hợp lệ";
        ErrorCode errorCode = ErrorCode.INVALID_MESSAGE_KEY;

        try {
            // Safely get field error
            FieldError fieldError = exception.getFieldError();
            if (fieldError != null) {
                enumKey = fieldError.getDefaultMessage();

                // If enumKey is null or empty, use field name + default message
                if (enumKey == null || enumKey.trim().isEmpty()) {
                    finalMessage = "Trường '" + fieldError.getField() + "' không hợp lệ";
                } else {
                    // Try to parse as ErrorCode enum
                    try {
                        errorCode = ErrorCode.valueOf(enumKey);
                        finalMessage = errorCode.getMessage();

                        // Try to get constraint attributes for message interpolation
                        try {
                            var constraintViolation = exception
                                    .getBindingResult()
                                    .getAllErrors()
                                    .get(0)
                                    .unwrap(ConstraintViolation.class);

                            Map<String, Object> attributes = constraintViolation
                                    .getConstraintDescriptor()
                                    .getAttributes();

                            if (attributes != null && !attributes.isEmpty()) {
                                finalMessage = mapAttribute(errorCode.getMessage(), attributes);
                                log.info("Constraint attributes: {}", attributes);
                            }
                        } catch (Exception e) {
                            log.warn("Could not extract constraint attributes: {}", e.getMessage());
                            // Keep the default error message
                        }

                    } catch (IllegalArgumentException e) {
                        // enumKey is not a valid ErrorCode, use it as custom message
                        finalMessage = enumKey;
                        log.warn("Invalid ErrorCode enum key: {}, using as custom message", enumKey);
                    }
                }
            } else {
                // No field error found, check if there are any errors
                if (!exception.getBindingResult().getAllErrors().isEmpty()) {
                    var error = exception.getBindingResult().getAllErrors().get(0);
                    finalMessage = error.getDefaultMessage() != null ? error.getDefaultMessage() : "Validation failed";
                }
            }

        } catch (Exception e) {
            log.error("Error processing MethodArgumentNotValidException: ", e);
            finalMessage = "Dữ liệu đầu vào không hợp lệ";
            errorCode = ErrorCode.INVALID_MESSAGE_KEY;
        }

        ApiResponse<Void> apiResponse = new ApiResponse<Void>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(finalMessage);

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String result = message;

        try {
            // Handle min attribute
            if (attributes.containsKey(MIN_ATTRIBUTES)) {
                String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTES));
                result = result.replace("{" + MIN_ATTRIBUTES + "}", minValue);
            }

            // Handle max attribute
            if (attributes.containsKey(MAX_ATTRIBUTES)) {
                String maxValue = String.valueOf(attributes.get(MAX_ATTRIBUTES));
                result = result.replace("{" + MAX_ATTRIBUTES + "}", maxValue);
            }

            // Handle value attribute (for current invalid value)
            if (attributes.containsKey("value")) {
                String value = String.valueOf(attributes.get("value"));
                result = result.replace("{value}", value);
            }

        } catch (Exception e) {
            log.warn("Error mapping attributes: {}", e.getMessage());
            return message; // Return original message if mapping fails
        }

        return result;
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

    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
    ResponseEntity<ApiResponse<Void>> responseEntity(InvalidDataAccessApiUsageException exception) {
        ErrorCode errorCode = ErrorCode.MISSING_INPUT;
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.<Void>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
