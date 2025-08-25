package com.fpl.datn.dto.request;

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
    int addressId;
    //    int paymentMethodId;
    String note;
    String inputFullname;
    String inputAddress;
    String inputPhone;
    //    LocalDate updatedAt;
    //    List<OrderItemResponse> items;
}
