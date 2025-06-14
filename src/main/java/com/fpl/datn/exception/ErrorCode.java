package com.fpl.datn.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZE_EXCEPTION(9999, "UNCATEGORIZE_EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR),
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
    CATEGORY_NOT_EXISTED(1019,"Category not existed", HttpStatus.BAD_REQUEST),
    UPLOAD_FILE_FAIL(1015, "Upload file to fail!", HttpStatus.BAD_REQUEST),
    REMOVE_FILE_FAIL(1016, "Remove file to fail!", HttpStatus.BAD_REQUEST),
    MISSING_INPUT(8888, "Missing input", HttpStatus.BAD_REQUEST),
    CIRCULAR_REFERENCE_NOT_ALLOWED(7777, "Circular reference not allow", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_CHILDREN(1022, "Category has child categories", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_PRODUCTS(1023, "Category has products", HttpStatus.BAD_REQUEST),
    CATEGORIES_NAME_EXISTED(1019, "Categories name existed", HttpStatus.CONFLICT),
    CATEGORIES_SLUG_EXISTED(1020, "Categories slug existed", HttpStatus.CONFLICT),
    PRODUCT_SLUG_EXISTED(1232, "Product slug existed", HttpStatus.CONFLICT),
    PRODUCT_NOT_EXISTED(1233, "Product create existed", HttpStatus.CONFLICT),
    PRODUCT_UPDATE_NOT_EXISTED(1234, "Product update existed", HttpStatus.CONFLICT),
    PRODUCT_DELETE_NOT_EXISTED(1235, "Product delete existed", HttpStatus.CONFLICT),
    PRODUCT_CREATE_VARIANT_EXISTED(1236, "Product_Variant create existed", HttpStatus.CONFLICT),
    PRODUCT_UPDATE_VARIANT_EXISTED(1237, "Product_Variant UPDATE existed", HttpStatus.CONFLICT),
    PRODUCT_DELETE_VARIANT_EXISTED(1238, "Product_Variant DELETE existed", HttpStatus.CONFLICT),
    PRODUCT_VARIANT_EXISTED(1239, "Product_Variant DELETE existed", HttpStatus.CONFLICT),
    PRODUCT_IMAGE_DETAIL_EXISTED(1240, "Product Image detail existed", HttpStatus.CONFLICT),
    PRODUCT_IMAGE_CREATE_EXISTED(1241, "Product Image Create existed", HttpStatus.CONFLICT),
    PRODUCT_IMAGE_UPDATE_EXISTED(1242, "Product Image Update existed", HttpStatus.CONFLICT),
    PRODUCT_IMAGE_DELETE_EXISTED(1243, "Product Image Delete existed", HttpStatus.CONFLICT),
    VARIANT_DETAIL_EXISTED(1244, "Variant detail existed", HttpStatus.CONFLICT),
    VARIANT_UPDATE_EXISTED(1245, "Variant update existed", HttpStatus.CONFLICT),
    VARIANT_DELETE_EXISTED(1246, "Variant delete existed", HttpStatus.CONFLICT),
    PRODUCT_VARIANT_VALUE_EXISTED(1247, "Variant delete existed", HttpStatus.CONFLICT),
    EMAIL_EXISTS(1018, "EMAIL EXITED", HttpStatus.BAD_REQUEST),
    PRODUCT_VARIANT_SKU_EXISTED(1248, "SKU đã tồn tại", HttpStatus.CONFLICT);

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
