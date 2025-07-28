package com.fpl.datn.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderStatus {
    PENDING("PENDING"), // đang xử lí
    CONFIRMED("CONFIRMED"), // đã xác nhận
    SHIPPED("SHIPPED"), // đang giao
    DELIVERED("DELIVERED"), // đã giao
    RECEIED("RECEIED"), // đã nhận
    CANCELLED("CANCELLED"); // đã hủy

    String description;
}
