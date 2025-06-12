package com.fpl.datn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    UNCATEGORIZE_EXCEPTION(9999, "UNCATEGORIZE_EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR),
    MISSING_INPUT(8888, "Missing input", HttpStatus.BAD_REQUEST),
    CIRCULAR_REFERENCE_NOT_ALLOWED(7777, "Circular reference not allow", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001, "INVALID MESSAGE KEY", HttpStatus.BAD_REQUEST),
    USER_EXITED(1002, "USER EXITED", HttpStatus.BAD_REQUEST),
    SELECTED_PRODUCT_NOT_EXISTED(1003, "SELECTED_PRODUCT_NOT_EXISTED", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1004, "USERNAME MUST AT LEES THAN {min} CHARACTER", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005, "PASSWORD NOT BE AT LEES THAN {min} CHARACTER", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1006, "USER_NOT_EXISTED", HttpStatus.NOT_FOUND),
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
    UPLOAD_FILE_FAIL(1019, "Upload file to fail!", HttpStatus.BAD_REQUEST),
    REMOVE_FILE_FAIL(1020, "Remove file to fail!", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_EXISTED(1021, "Category not existed", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_CHILDREN(1022, "Category has child categories", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_PRODUCTS(1023, "Category has products", HttpStatus.BAD_REQUEST),
    CATEGORIES_NAME_EXISTED(1024, "Categories name existed", HttpStatus.CONFLICT),
    CATEGORIES_SLUG_EXISTED(1025, "Categories slug existed", HttpStatus.CONFLICT),
    PRODUCT_SLUG_EXISTED(1026, "Product slug existed", HttpStatus.CONFLICT),
    PRODUCT_NOT_EXISTED(1027, "Product create existed", HttpStatus.CONFLICT),
    PRODUCT_UPDATE_NOT_EXISTED(1028, "Product update existed", HttpStatus.CONFLICT),
    PRODUCT_DELETE_NOT_EXISTED(1029, "Product delete existed", HttpStatus.CONFLICT),
    PRODUCT_REVIEW_NOT_FOUND(1030, "Product review not found", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_FOUND(1031, "Voucher not found", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_NOT_FOUND(1032, "Product variant not found", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(1033, "Product is out of stock", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1034, "Product not found", HttpStatus.BAD_REQUEST),
    SELECTED_PRODUCT_NOT_FOUND(1035, "Selected product not found", HttpStatus.BAD_REQUEST),

    // Order
    ORDER_NOT_FOUND(1036, "Order not found", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_PROCESSED(1037, "Order has already been processed", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_BE_MODIFIED(1038, "Order cannot be modified", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_NOT_FOUND(1039, "Order status not found", HttpStatus.BAD_REQUEST),

    // Address
    ADDRESS_NOT_FOUND(1040, "Address not found", HttpStatus.BAD_REQUEST),

    // Payment
    PAYMENT_METHOD_NOT_FOUND(1041, "Payment method not found", HttpStatus.BAD_REQUEST),
    PAYMENT_STATUS_NOT_FOUND(1042, "Payment status not found", HttpStatus.BAD_REQUEST),

    CART_ITEM_ALREADY_EXISTS(1043, "Cart item already exists", HttpStatus.BAD_REQUEST),

    // User-Voucher errors
    USER_VOUCHER_NOT_FOUND(1044, "User voucher not found", HttpStatus.NOT_FOUND),
    USER_VOUCHER_ALREADY_EXISTS(1045, "User already has this voucher", HttpStatus.BAD_REQUEST),
    SOME_USERS_NOT_FOUND(1046, "Some users not found", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1047, "User not found", HttpStatus.NOT_FOUND),

    // Voucher validation errors - THÊM MỚI
    VOUCHER_INACTIVE(1048, "Voucher is inactive", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_STARTED(1049, "Voucher has not started yet", HttpStatus.BAD_REQUEST),
    VOUCHER_EXPIRED(1050, "Voucher has expired", HttpStatus.BAD_REQUEST),
    VOUCHER_OUT_OF_STOCK(1051, "Voucher is out of stock", HttpStatus.BAD_REQUEST),
    ORDER_VALUE_TOO_LOW(1052, "Order value is too low for this voucher", HttpStatus.BAD_REQUEST),

    // Activity log
    ACTIVITY_LOG_NOT_FOUND(1053, "Activity log not found", HttpStatus.NOT_FOUND),

    // Cart errors
    CART_NOT_FOUND(1054, "Cart not found", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(1055, "Cart item not found", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_BELONG_TO_USER(1056, "Cart item does not belong to user", HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    ErrorCode() {}
}
