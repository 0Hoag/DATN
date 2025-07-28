package com.fpl.datn.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PaymentStatus {
    PENDING("PENDING"), // đang xử lí
    PAID("PAID"), // đã thanh toán
    REFUNDED("REFUNDED"),
    FAILED("FAILED");

    String description;
}
