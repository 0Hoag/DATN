package com.fpl.datn.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderStatus {
    PENDING("PENDING"),
    CONFIRMED("CONFIRMED"),
    SHIPPED("SHIPPED"),
    DELIVERED("DELIVERED"),
    RECEIED("RECEIED"),
    CANCELLED("CANCELLED");

    String description;
}
