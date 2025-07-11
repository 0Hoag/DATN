package com.fpl.datn.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

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
    @NotNull(message = "USERID_NOT_NULL")
    Integer userId;

    Integer addressId;
    String inputAddress;
    String inputFullname;
    String inputPhone;
    Integer paymentMethodId;
    String orderStatus;
    String paymentStatus;
    Integer voucherId;
    String note;
    List<OrderItemResponse> items;
}
