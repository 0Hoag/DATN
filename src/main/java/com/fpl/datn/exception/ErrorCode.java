package com.fpl.datn.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    // User (1000–1099)
    USER_ALREADY_EXISTS(1001, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_USED(1003, "Email is already in use", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1004, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1005, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_DOB(1006, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1007, "User does not exist", HttpStatus.NOT_FOUND),
    PASSWORD_ALREADY_USED(1008, "Password has already been used", HttpStatus.BAD_REQUEST),
    ERROR_CREATING_USER(1009, "Error while creating user", HttpStatus.BAD_REQUEST),
    ERROR_UPDATING_USER(1010, "Error while updating user", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1011, "Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(1012, "Phone already exists", HttpStatus.BAD_REQUEST),
    EMAIL_UNCHANGED(1013, "Email is the same as the current one", HttpStatus.BAD_REQUEST),
    PHONE_UNCHANGED(1014, "Phone is the same as the current one", HttpStatus.BAD_REQUEST),
    EMAIL_INCORRECT(1015, "Email incorrect", HttpStatus.BAD_REQUEST),
    PASSWORD_INCORRECT(1015, "Password is incorrect", HttpStatus.BAD_REQUEST),
    ERROR_CREATE_USER(1016, "Error create user", HttpStatus.BAD_REQUEST),
    ERROR_UPDATE_USER(1017, "Error update user", HttpStatus.BAD_REQUEST),
    OLD_PASSWORD_INCORRECT(1019, "OLD_PASSWORD_INCORRECT", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_NOT_DUPLICATE_CONFIRM_PASSWORD(
            1020, "NEW_PASSWORD_NOT_DUPLICATE_CONFIRM_PASSWORD", HttpStatus.BAD_REQUEST),

    // Permission (1100–1199)
    PERMISSION_NOT_FOUND(1101, "Permission not found", HttpStatus.BAD_REQUEST),

    // Order (1200–1299)
    ORDER_NOT_FOUND(1201, "Order not found", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_PROCESSED(1202, "Order has already been processed", HttpStatus.BAD_REQUEST),
    ORDERS_NOT_EXISTED(1203, "Orders do not exist", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_BE_MODIFIED(1204, "Order cannot be modified", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_NOT_FOUND(1205, "Order status not found", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_STATUS(1206, "Order must be in RECEIED status to perform this action", HttpStatus.BAD_REQUEST),
    RETURN_PERIOD_EXPIRED(1207, "The return period has expired", HttpStatus.BAD_REQUEST),
    RETURN_REQUEST_ALREADY_EXISTS(1208, "A return request already exists for this order", HttpStatus.BAD_REQUEST),
    ORDER_RETURN_NOT_FOUND(1209, "Order return not found", HttpStatus.BAD_REQUEST),
    ORDER_RETURN_STATUS_NOT_FOUND(1210, "Order return status not found", HttpStatus.BAD_REQUEST),
    VNPAY_REFUND_FAILED(1210, "VNPAY REFUND FAILED", HttpStatus.BAD_REQUEST),
    ORDER_DELETE_RECEIVED(1211, "Cannot delete an order that has already been delivered", HttpStatus.BAD_REQUEST),
    ORDER_DELETE_PAID(1212, "Cannot delete an order that has already been paid", HttpStatus.BAD_REQUEST),

    // Address (1300–1399)
    ADDRESS_NOT_FOUND(1301, "Address not found", HttpStatus.BAD_REQUEST),

    // Payment (1400–1499)
    PAYMENT_METHOD_NOT_FOUND(1401, "Payment method not found", HttpStatus.BAD_REQUEST),
    PAYMENT_STATUS_NOT_FOUND(1402, "Payment status not found", HttpStatus.BAD_REQUEST),

    // Product / Variant (1500–1599)
    PRODUCT_VARIANT_NOT_FOUND(1501, "Product variant not found", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(1502, "Product is out of stock", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1503, "Product not found", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_EXISTED(1504, "Product name already exists", HttpStatus.BAD_REQUEST),
    SELECTED_PRODUCT_NOT_FOUND(1505, "Selected product not found", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_EXISTED(1506, "Product variant already exists", HttpStatus.CONFLICT),
    PRODUCT_VARIANT_VALUE_EXISTED(1510, "Product variant value already exists", HttpStatus.CONFLICT),
    VARIANT_DETAIL_EXISTED(1511, "Variant detail already exists", HttpStatus.CONFLICT),
    PRODUCT_VARIANT_SKU_EXISTED(1512, "Product SKU already exists", HttpStatus.CONFLICT),
    VARIANT_UPDATE_EXISTED(1513, "Variant update already exists", HttpStatus.CONFLICT),
    VARIANT_DELETE_EXISTED(1514, "Variant delete already exists", HttpStatus.CONFLICT),
    PRODUCT_SLUG_EXISTED(1515, "Product slug already exists", HttpStatus.CONFLICT),
    PRODUCT_NOT_EXISTED(1516, "Product creation failed", HttpStatus.CONFLICT),
    PRODUCT_UPDATE_NOT_EXISTED(1517, "Product update failed", HttpStatus.CONFLICT),
    PRODUCT_DELETE_NOT_EXISTED(1518, "Product delete failed", HttpStatus.CONFLICT),
    PRODUCT_DELETE_EXISTED(1995, "Product had uses", HttpStatus.BAD_REQUEST),
    PRODUCT_REVIEW_NOT_FOUND(1519, "Product review not found", HttpStatus.BAD_REQUEST),
    VARIANT_VALUE_NOT_FOUND(1520, "Variant attribute not found", HttpStatus.CONFLICT),
    VARIANT_NOT_EXISTED(1521, "Variant not found", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_CANNOT_BE_MODIFIED(1522, "Order status cannot be modified", HttpStatus.BAD_REQUEST),
    DUPLICATE_ATTRIBUTE_VALUE(1523, "Duplicate attribute value", HttpStatus.BAD_REQUEST),
    ATTRIBUTE_VALUE_ALREADY_EXISTS(1524, "Attribute Value EXISTED", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_UPLOAD_ID_REQUIRED(1525, "Upload image ID must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_ID_REQUIRED(1526, "Image ID must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_ALT_TEXT_REQUIRED(1071, "Alt text must not be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_DESCRIPTION_REQUIRED(1072, "Specification description must not be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_IS_THUMBNAIL_REQUIRED(1073, "Thumbnail status must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_SORT_ORDER_REQUIRED(1074, "Image sort order must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_VARIANT_ID_REQUIRED(1075, "Product variant ID must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_URL_REQUIRED(1076, "Image URL must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_ID_REQUIRED(1540, "Product variant ID must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_NAME_REQUIRED(1541, "Product variant name must not be blank", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_PRICE_REQUIRED(1542, "Product price must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_QUANTITY_REQUIRED(1543, "Product quantity must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_SOLD_REQUIRED(1544, "Sold quantity must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_ISACTIVE_REQUIRED(1545, "Active status must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_PRODUCT_ID_REQUIRED(1546, "Product ID must not be null", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_ATTRIBUTE_VALUE_IDS_REQUIRED(1547, "Attribute value list must not be null", HttpStatus.BAD_REQUEST),

    // Cart (1600–1699)
    CART_ITEM_ALREADY_EXISTS(1601, "Cart item already exists", HttpStatus.BAD_REQUEST),

    // File (1700–1799)
    FILE_UPLOAD_FAILED(1701, "Failed to upload file", HttpStatus.BAD_REQUEST),
    FILE_DELETE_FAILED(1702, "Failed to delete file", HttpStatus.BAD_REQUEST),
    IMAGE_NOT_FOUND(1703, "Image not found", HttpStatus.BAD_REQUEST),

    // Account (1800–1899)
    EMAIL_ALREADY_EXISTS(1801, "Email already exists", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXISTS(1802, "Phone number already exists", HttpStatus.BAD_REQUEST),

    // Auth (1900–1999)
    UNAUTHENTICATED(1901, "Unauthenticated access", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1902, "You do not have permission", HttpStatus.FORBIDDEN),

    // Validation / Input (2000–2099)
    INVALID_INPUT(2001, "Invalid input data", HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_KEY(2002, "Invalid message key", HttpStatus.BAD_REQUEST),
    USERID_NOT_NULL(2003, "UserId cannot be null", HttpStatus.BAD_REQUEST),
    FULLNAME_NOT_NULL(2004, "Name cannot be null", HttpStatus.BAD_REQUEST),
    PHONE_NOT_NULL(2005, "Phone cannot be null", HttpStatus.BAD_REQUEST),

    // Voucher (2100–2199)
    VOUCHER_NOT_FOUND(2101, "Voucher not found", HttpStatus.BAD_REQUEST),
    VOUCHER_EXPIRED(2102, "Voucher expired", HttpStatus.BAD_REQUEST),
    VOUCHER_MIN_ORDER_NOT_MET(2103, "Order value does not meet minimum for voucher", HttpStatus.BAD_REQUEST),
    VOUCHER_EXISTED(2104, "You already have this voucher", HttpStatus.CONFLICT),
    VOUCHER_INVALID(2105, "Voucher is invalid", HttpStatus.BAD_REQUEST),
    VOUCHER_OVERUSED(2106, "Voucher has been overused", HttpStatus.BAD_REQUEST),

    // Category (2200–2299)
    CATEGORY_NOT_EXISTED(2201, "Category does not exist", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_CHILDREN(2202, "Category has child categories", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_PRODUCTS(2203, "Category has products", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_EXISTED(2204, "Category name already exists", HttpStatus.CONFLICT),
    CATEGORY_SLUG_EXISTED(2205, "Category slug already exists", HttpStatus.CONFLICT),

    // Transactionlog(2300-2399)
    TRANCSACTION_LOG_NOT_FOUND(2301, "Transaction log not found", HttpStatus.NOT_FOUND),

    // Common (2500–2599)
    RESOURCE_NOT_FOUND(2501, "Resource not found", HttpStatus.NOT_FOUND),

    // System (7000+)
    CIRCULAR_REFERENCE_NOT_ALLOWED(7777, "Circular reference not allowed", HttpStatus.BAD_REQUEST),
    MISSING_INPUT(8888, "Missing input", HttpStatus.BAD_REQUEST),
    UNKNOWN_ERROR(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatus httpStatus;
}
