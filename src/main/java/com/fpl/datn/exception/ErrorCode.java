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
    // User
    USER_NOT_EXIST(2001, "User does not exist", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_USED(2002, "Email is already in use", HttpStatus.BAD_REQUEST),

    // Order
    ORDER_NOT_EXIST(3001, "Order does not exist", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_PROCESSED(3002, "Order has already been processed", HttpStatus.BAD_REQUEST),

    // Address
    ADDRESS_NOT_EXIST(4001, "Address does not exist", HttpStatus.BAD_REQUEST),

    // Payment
    PAYMENT_METHOD_NOT_EXIST(5001, "Payment method does not exist", HttpStatus.BAD_REQUEST),

    // Product / Variant
    PRODUCT_VARIANT_NOT_EXIST(6001, "Product variant does not exist", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(6002, "Product variant is out of stock", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXIST(6003, "Product does not exist", HttpStatus.BAD_REQUEST),

    // Common
    INTERNAL_ERROR(9999, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT(1001, "Invalid input data", HttpStatus.BAD_REQUEST),
    NOT_FOUND(1002, "Resource not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(1003, "User is not authenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1004, "Access is denied", HttpStatus.FORBIDDEN),
    ;

    int code;
    String message;
    HttpStatus httpStatus;
}
