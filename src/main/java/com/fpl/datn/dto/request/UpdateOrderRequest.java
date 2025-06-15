package com.fpl.datn.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.fpl.datn.dto.response.OrderItemResponse;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateOrderRequest {
    int userId;
    int addressId;
    int paymentMethodId;
    String note;
    LocalDate updatedAt;
    List<OrderItemResponse> items;
}
