package com.fpl.datn.constant;

public class PredefinedRole {
    public static final String ROLE_GUEST = "GUEST"; // "VIEW_PRODUCT"
    public static final String ROLE_CUSTOMER = "CUSTOMER"; // "TRACK_ORDER","CREATE_REVIEW","BUY_PRODUCT","VIEW_PRODUCT", "VIEW_ORDER"
    public static final String ROLE_SHIFT_STAFF = "SHIFT_STAFF"; // "VIEW_ORDER", "UPDATE_ORDER_STATUS", "VIEW_SHIPPING_INFO"
    public static final String ROLE_MANAGER = "MANAGER"; // `MANAGE_USERS`, `MANAGE_PRODUCTS`, `MANAGE_ORDERS`,
    public static final String ROLE_ADMIN = "ADMIN"; // `ASSIGN_ROLE`, `SYSTEM_SETTINGS`, `SYSTEM_BACKUP`, `VIEW_DASHBOARD`

    private PredefinedRole() {}
}
