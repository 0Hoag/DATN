package com.fpl.datn.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderActionType {
    //    CREATE("Create"),
    //    UPDATE("Update"),
    //    DELETE("Delete"),
    //    REFUND("REFUND"),
    //    VNPAY_SUCCESS("VNPAY SUCCESS"),
    //    REFUND_SUCCESS("REFUND SUCCESS"),
    //    REFUND_FAILED("REFUND FAILED"),
    //    UPDATESTATUS("UPDATE STATUS");

    // Đơn hàng
    CREATE("CREATE ORDER"),
    UPDATE("UPDATE ORDER"),
    CANCELLED("CANCELLED"),
    DELETE("DELETE ORDER"),
    UPDATE_STATUS("UPDATE STATUS"),

    // Thanh toán
    PAYMENT_SUCCESS("PAYMENT SUCCESS"),
    PAYMENT_FAILED("PAYMENT FAILED"),

    // Hoàn tiền
    REFUND_REQUEST("REQUEST REFUND"),
    REFUND_SUCCESS("REFUND SUCCESS"),
    REFUND_FAILED("REFUND FAILED");

    String type;
}
