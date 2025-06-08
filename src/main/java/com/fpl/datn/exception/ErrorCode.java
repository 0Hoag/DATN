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
    USER_ALREADY_EXISTS(1001, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_USED(1003, "Email is already in use", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1004, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1006, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PASSWORD_ALREADY_USED(1007, "Password has already been used", HttpStatus.BAD_REQUEST),
    ERROR_CREATING_USER(1008, "Error while creating user", HttpStatus.BAD_REQUEST),
    ERROR_UPDATING_USER(1009, "Error while updating user", HttpStatus.BAD_REQUEST),

    // Permission
    PERMISSION_NOT_FOUND(1101, "Permission not found", HttpStatus.BAD_REQUEST),

    // Order
    ORDER_NOT_FOUND(1201, "Order not found", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_PROCESSED(1202, "Order has already been processed", HttpStatus.BAD_REQUEST),
    ORDERS_NOT_EXISTED(1203, "Orders not existed", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_BE_MODIFIED(1204, "Order cannot be modified", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_NOT_FOUND(1205, "Order status not found", HttpStatus.BAD_REQUEST),

    // Address
    ADDRESS_NOT_FOUND(1301, "Address not found", HttpStatus.BAD_REQUEST),

    // Payment
    PAYMENT_METHOD_NOT_FOUND(1401, "Payment method not found", HttpStatus.BAD_REQUEST),
    PAYMENT_STATUS_NOT_FOUND(1401, "Payment status not found", HttpStatus.BAD_REQUEST),

    // Product / Variant / Product Review
    PRODUCT_VARIANT_NOT_FOUND(1501, "Product variant not found", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(1502, "Product is out of stock", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1503, "Product not found", HttpStatus.BAD_REQUEST),
    SELECTED_PRODUCT_NOT_FOUND(1504, "Selected product not found", HttpStatus.BAD_REQUEST),
    PRODUCT_REVIEW_NOT_FOUND(1505, "Product review not found", HttpStatus.BAD_REQUEST),

    // Cart
    CART_ITEM_ALREADY_EXISTS(1601, "Cart item already exists", HttpStatus.BAD_REQUEST),

    // File
    FILE_UPLOAD_FAILED(1701, "Failed to upload file", HttpStatus.BAD_REQUEST),
    FILE_DELETE_FAILED(1702, "Failed to delete file", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_FOUND(1703, "Image not found", HttpStatus.BAD_REQUEST),

    // Account
    EMAIL_ALREADY_EXISTS(1801, "Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS(1802, "Phone number already exists", HttpStatus.BAD_REQUEST),

    // Authentication & Authorization
    UNAUTHENTICATED(1901, "Unauthenticated access", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1902, "You do not have permission", HttpStatus.FORBIDDEN),

    // Input
    INVALID_INPUT(2001, "Invalid input data", HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_KEY(2002, "Invalid message key", HttpStatus.BAD_REQUEST),

    // Voucher
    VOUCHER_NOT_FOUND(2101, "Voucher not found", HttpStatus.BAD_REQUEST),

    // System / Common
    RESOURCE_NOT_FOUND(2501, "Resource not found", HttpStatus.NOT_FOUND),
    UNKNOWN_ERROR(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatus httpStatus;
}
