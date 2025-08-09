package com.fpl.datn.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderReturnResponse {
    int id;
    LocalDateTime createdAt;
    int paymentMethod;
    String status;
    String reason;
    BigDecimal refundAmount;

    String fullName;
    OrderResponse order;
}
