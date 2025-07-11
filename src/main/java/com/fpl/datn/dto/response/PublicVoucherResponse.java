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
public class PublicVoucherResponse {
    String code;
    String description;
    BigDecimal discountValue;
    BigDecimal minOrderValue;
    Integer remainingQuantity;
    LocalDateTime endAt;
    Boolean isActive;
    Boolean canClaim; // User có thể claim không
}