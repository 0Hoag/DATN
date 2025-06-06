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
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),
    NOT_FOUND(1002, "Not Found", HttpStatus.NOT_FOUND),
    USER_NOT_EXISTED(1003, "User not existed", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1004, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_EXISTED(1005, "Username not existed", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_EXISTED(1006, "Permission not existed", HttpStatus.BAD_REQUEST),
    PERMISSION_EXISTED(1007, "Permission existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1008, "Role not existed", HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1009, "Role existed", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1010, "password must be at least 6 character", HttpStatus.BAD_REQUEST),
    DOB_INVALID(1011, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATE(1012, "Unauthorized", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(1013, "You do not have permission", HttpStatus.FORBIDDEN),
    ORDER_NOT_EXISTED(1014, "Order not existed", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_EXISTED(1015, "Address not existed", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_EXISTED(1016, "Payment method not existed", HttpStatus.BAD_REQUEST),
    PRODUCTVARIANT_NOT_EXISTED(1016, "Product variant not existed", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(1017, "Product variant is out of stock", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(9999, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    int code;
    String message;
    HttpStatus httpStatus;
}
