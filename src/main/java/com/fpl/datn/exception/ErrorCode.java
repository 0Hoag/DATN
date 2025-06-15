package com.fpl.datn.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZE_EXCEPTION(9999, "UNCATEGORIZE_EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "INVALID MESSAGE KEY", HttpStatus.BAD_REQUEST),
    USER_EXITED(1002, "USER EXITED", HttpStatus.BAD_REQUEST),
    SELECTED_PRODUCT_NOT_EXISTED(1003, "SELECTED_PRODUCT_NOT_EXISTED", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1004, "USERNAME MUST AT LEES THAN {min} CHARACTER", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005, "PASSWORD NOT BE AT LEES THAN {min} CHARACTER", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "USER_NOT_EXISTED", HttpStatus.NOT_FOUND),
    PASSWORD_INCORRECT(1006, "PASSWORD_INCORRECT", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, "UNAUTHENTICATED", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZE(1008, "YOU DO NOT HAVE PREMISSION", HttpStatus.FORBIDDEN),
    INVALID_DOB(1009, "YOU AGE MUST BE AT least {min}", HttpStatus.BAD_REQUEST),
    PASSWORD_EXISTED(1010, "PASSWORD_EXISTED", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1011, "PERMISSION_NOT_FOUND", HttpStatus.BAD_REQUEST),
    CART_ITEM_EXISTED(1012, "CART_ITEM_EXISTED", HttpStatus.BAD_REQUEST),
    ORDERS_NOT_EXISTED(1013, "ORDERS_NOT_EXISTED", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_EXISTED(1014, "Image not existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1015, "Email existed", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(1016, "Phone existed", HttpStatus.BAD_REQUEST),
    ERROR_CREATE_USER(1017, "Error create user", HttpStatus.BAD_REQUEST),
    ERROR_UPDATE_USER(1018, "Error update user", HttpStatus.BAD_REQUEST),

    // Account
    EMAIL_ALREADY_EXISTS(1801, "Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS(1802, "Phone number already exists", HttpStatus.BAD_REQUEST),

    // Authentication & Authorization
    UNAUTHENTICATED(1901, "Unauthenticated access", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1902, "You do not have permission", HttpStatus.FORBIDDEN),

    // Input
    INVALID_INPUT(2001, "Invalid input data", HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_KEY(2002, "Invalid message key", HttpStatus.BAD_REQUEST),

    // System / Common
    RESOURCE_NOT_FOUND(2101, "Resource not found", HttpStatus.NOT_FOUND),
    UNKNOWN_ERROR(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatus httpStatus;
}
