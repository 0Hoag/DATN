package com.fpl.datn.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OrderReturnEnums {
    PENDING("PENDING"),
    REFUNDED("REFUNDED"),
    REJECTED("REJECTED");
    String description;
}
