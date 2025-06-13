package com.fpl.datn.dto.request;

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
public class OrderRequest {
    Integer userId;
    Integer addressId;
    Integer paymentMethodId;
    Integer voucherId;
    String note;
    List<OrderItemResponse> items;
}
