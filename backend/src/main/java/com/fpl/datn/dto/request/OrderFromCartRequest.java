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
public class OrderFromCartRequest {
    Integer addressId;

    String inputAddress;
    String inputFullname;
    String inputPhone;

    Integer paymentMethodId;

    String orderStatus;
    String paymentStatus;
    Integer voucherId;
    String note;
}
