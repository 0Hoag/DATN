package com.fpl.datn.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherClaimResponse {
    String message;
    String voucherCode;
    String description;
    String discountValue;
    Boolean success;
    Integer remainingUses;
}
