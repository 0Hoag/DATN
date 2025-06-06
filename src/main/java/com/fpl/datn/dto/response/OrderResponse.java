package com.fpl.datn.dto.response;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Integer id;
    LocalDate createdAt;
    String fullName;
    String phone;
    String note;
    String orderStatus;
    String paymentMethod;
    String paymentStatus;
}
