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
    UNKNOWN_ERROR(9000, "Unknown server error", HttpStatus.INTERNAL_SERVER_ERROR),
    MISSING_INPUT(8888, "Missing input", HttpStatus.BAD_REQUEST),
    CIRCULAR_REFERENCE_NOT_ALLOWED(7777, "Circular reference not allow", HttpStatus.BAD_REQUEST),

    // User
    INVALID_KEY(1001, "INVALID MESSAGE KEY", HttpStatus.BAD_REQUEST),
    USER_EXITED(1002, "USER EXITED", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003, "USER NOT FOUND", HttpStatus.BAD_REQUEST),
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
    IMAGE_NOT_EXISTED(1014, "Image not existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1015, "Email existed", HttpStatus.BAD_REQUEST),
    EMAIL_UNCHANGED(1015, "Email is the same as the current one", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(1016, "Phone existed", HttpStatus.BAD_REQUEST),
    PHONE_UNCHANGED(1015, "Phone is the same as the current one", HttpStatus.BAD_REQUEST),
    ERROR_CREATE_USER(1017, "Error create user", HttpStatus.BAD_REQUEST),
    ERROR_UPDATE_USER(1018, "Error update user", HttpStatus.BAD_REQUEST),

    // Category
    CATEGORY_NOT_EXISTED(1019,"Category not existed", HttpStatus.BAD_REQUEST),
    CATEGORIES_NAME_EXISTED(1020, "Categories name existed", HttpStatus.CONFLICT),
    CATEGORIES_SLUG_EXISTED(1021, "Categories slug existed", HttpStatus.CONFLICT),
    CATEGORY_HAS_CHILDREN(1022, "Category has child categories", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_PRODUCTS(1023, "Category has products", HttpStatus.BAD_REQUEST),

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

    // Product / Variant
    PRODUCT_VARIANT_NOT_FOUND(1501, "Product variant not found", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(1502, "Product is out of stock", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND(1503, "Product not found", HttpStatus.BAD_REQUEST),
    SELECTED_PRODUCT_NOT_FOUND(1504, "Selected product not found", HttpStatus.BAD_REQUEST),
    PRODUCT_SLUG_EXISTED(1505, "Product slug existed", HttpStatus.CONFLICT),
    PRODUCT_UPDATE_NOT_EXISTED(1506, "Product update existed", HttpStatus.CONFLICT),
    PRODUCT_DELETE_NOT_EXISTED(1507, "Product delete existed", HttpStatus.CONFLICT),
    PRODUCT_REVIEW_NOT_FOUND(1508, "Product review not found", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_DETAIL_EXISTED(1240, "Product Image detail existed", HttpStatus.CONFLICT),
    PRODUCT_IMAGE_CREATE_EXISTED(1241, "Product Image Create existed", HttpStatus.CONFLICT),
    PRODUCT_IMAGE_UPDATE_EXISTED(1242, "Product Image Update existed", HttpStatus.CONFLICT),
    PRODUCT_IMAGE_DELETE_EXISTED(1243, "Product Image Delete existed", HttpStatus.CONFLICT),
    VARIANT_DETAIL_EXISTED(1244, "Variant detail existed", HttpStatus.CONFLICT),
    VARIANT_UPDATE_EXISTED(1245, "Variant update existed", HttpStatus.CONFLICT),
    VARIANT_DELETE_EXISTED(1246, "Variant delete existed", HttpStatus.CONFLICT),
    PRODUCT_VARIANT_VALUE_EXISTED(1247, "Variant delete existed", HttpStatus.CONFLICT),
    EMAIL_EXISTS(1018, "EMAIL EXITED", HttpStatus.BAD_REQUEST),
    UPLOAD_IMAGE_FAILED(1018, "UPLOAD IMAGE EXITED", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_SKU_EXISTED(1248, "SKU đã tồn tại", HttpStatus.CONFLICT),
    PRODUCT_VARIANT_EXISTED(1239, "Product_Variant DELETE existed", HttpStatus.CONFLICT),

    // Cart
    CART_ITEM_ALREADY_EXISTS(1601, "Cart item already exists", HttpStatus.BAD_REQUEST),

    // Input
    INVALID_INPUT(2001, "Invalid input data", HttpStatus.BAD_REQUEST),
    INVALID_MESSAGE_KEY(2002, "Invalid message key", HttpStatus.BAD_REQUEST),
    CATEGORY_CHILD_CANNOT_HAVE_PARENT(1024, "Category child cannot have parent", HttpStatus.BAD_REQUEST),
    
    // Voucher
    VOUCHER_NOT_FOUND(2101, "Voucher not found", HttpStatus.BAD_REQUEST),
    DASHBOARD_ERROR(1044, "Dashboard statistics retrieval error", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatus httpStatus;
}
