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
    USER_NOT_EXISTED(1006, "USER_NOT_EXISTED", HttpStatus.NOT_FOUND),
    PASSWORD_ALREADY_USED(1007, "Password has already been used", HttpStatus.BAD_REQUEST),
    ERROR_CREATING_USER(1008, "Error while creating user", HttpStatus.BAD_REQUEST),
    ERROR_UPDATING_USER(1009, "Error while updating user", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1010, "Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(1011, "Phone already exists", HttpStatus.BAD_REQUEST),
    ERROR_CREATE_USER(1017, "Error create user", HttpStatus.BAD_REQUEST),
    ERROR_UPDATE_USER(1018, "Error update user", HttpStatus.BAD_REQUEST),
    EMAIL_UNCHANGED(1015, "Email is the same as the current one", HttpStatus.BAD_REQUEST),
    PHONE_UNCHANGED(1015, "Phone is the same as the current one", HttpStatus.BAD_REQUEST),
    PASSWORD_INCORRECT(1006, "PASSWORD_INCORRECT", HttpStatus.BAD_REQUEST),

    PRODUCT_SLUG_EXISTED(1026, "Product slug existed", HttpStatus.CONFLICT),
    PRODUCT_NOT_EXISTED(1027, "Product create existed", HttpStatus.CONFLICT),
    PRODUCT_UPDATE_NOT_EXISTED(1028, "Product update existed", HttpStatus.CONFLICT),
    PRODUCT_DELETE_NOT_EXISTED(1029, "Product delete existed", HttpStatus.CONFLICT),
    PRODUCT_REVIEW_NOT_FOUND(1030, "Product review not found", HttpStatus.BAD_REQUEST),

    // Permission
    PERMISSION_NOT_FOUND(1101, "Permission not found", HttpStatus.BAD_REQUEST),

    // Order, Order Return
    ORDER_NOT_FOUND(1201, "Order not found", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_PROCESSED(1202, "Order has already been processed", HttpStatus.BAD_REQUEST),
    ORDERS_NOT_EXISTED(1203, "Orders not existed", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_BE_MODIFIED(1204, "Order cannot be modified", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_NOT_FOUND(1205, "Order status not found", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS(1206, "Order must be in DELIVERED status to perform this action", HttpStatus.BAD_REQUEST),
    RETURN_PERIOD_EXPIRED(1207, "The return period 7 days has expired for this order", HttpStatus.BAD_REQUEST),
    RETURN_REQUEST_ALREADY_EXISTS(1208, "A return request for this order already exists", HttpStatus.BAD_REQUEST),
    ORDER_RETURN_NOT_FOUND(1209, "Order return not found", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_CANNOT_BE_MODIFIED(1204, "Order status cannot be modified", HttpStatus.BAD_REQUEST),
    ORDER_RETURN_STATUS_NOT_FOUND(1210, "Order return status not found", HttpStatus.BAD_REQUEST),

    // Address
    ADDRESS_NOT_FOUND(1301, "Address not found", HttpStatus.BAD_REQUEST),

    // Payment
    PAYMENT_METHOD_NOT_FOUND(1401, "Payment method not found", HttpStatus.BAD_REQUEST),
    PAYMENT_STATUS_NOT_FOUND(1401, "Payment status not found", HttpStatus.BAD_REQUEST),

    // Product / Variant
    PRODUCT_VARIANT_NOT_FOUND(1501, "Product variant not found", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(1502, "Product is out of stock", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1503, "Product not found", HttpStatus.BAD_REQUEST),
    SELECTED_PRODUCT_NOT_FOUND(1504, "Selected product not found", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_EXISTED(1239, "Product_Variant DELETE existed", HttpStatus.CONFLICT),
    UPLOAD_IMAGE_FAILED(1018, "UPLOAD IMAGE EXITED", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_DETAIL_EXISTED(1240, "Product Image detail existed", HttpStatus.CONFLICT),
    PRODUCT_IMAGE_UPDATE_EXISTED(1242, "Product Image Update existed", HttpStatus.CONFLICT),
    PRODUCT_IMAGE_DELETE_EXISTED(1243, "Product Image Delete existed", HttpStatus.CONFLICT),
    PRODUCT_VARIANT_VALUE_EXISTED(1247, "Variant delete existed", HttpStatus.CONFLICT),
    VARIANT_DETAIL_EXISTED(1244, "Variant detail existed", HttpStatus.CONFLICT),
    PRODUCT_DELETE_VARIANT_EXISTED(1238, "Product_Variant DELETE existed", HttpStatus.CONFLICT),
    PRODUCT_VARIANT_SKU_EXISTED(1248, "SKU existed", HttpStatus.CONFLICT),
    VARIANT_UPDATE_EXISTED(1245, "Variant update existed", HttpStatus.CONFLICT),
    VARIANT_DELETE_EXISTED(1246, "Variant delete existed", HttpStatus.CONFLICT),

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
    VOUCHER_EXPIRED(2102, "Voucher expried", HttpStatus.BAD_REQUEST),
    VOUCHER_MIN_ORDER_NOT_MET(
            2103, "The order value does not meet the minimum required to apply this voucher", HttpStatus.BAD_REQUEST),
    VOUCHER_EXISTED(2104, "You already have this voucher", HttpStatus.CONFLICT),
    VOUCHER_INVALID(2105, "Voucher invalid", HttpStatus.BAD_REQUEST),
    VOUCHER_ORVERUSED(2106, "Voucher Overused", HttpStatus.BAD_REQUEST),

    // Category
    CATEGORY_NOT_EXISTED(2201, "Category not existed", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_CHILDREN(2202, "Category has child categories", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_PRODUCTS(2203, "Category has products", HttpStatus.BAD_REQUEST),
    CATEGORIES_NAME_EXISTED(2204, "Categories name existed", HttpStatus.CONFLICT),
    CATEGORIES_SLUG_EXISTED(2205, "Categories slug existed", HttpStatus.CONFLICT),

    // System / Common
    RESOURCE_NOT_FOUND(2501, "Resource not found", HttpStatus.NOT_FOUND),

    // Others
    CIRCULAR_REFERENCE_NOT_ALLOWED(7777, "Circular reference not allow", HttpStatus.BAD_REQUEST),
    MISSING_INPUT(8888, "Missing input", HttpStatus.BAD_REQUEST),
    UNKNOWN_ERROR(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatus httpStatus;
}
