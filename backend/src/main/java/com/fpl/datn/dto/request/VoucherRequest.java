package com.fpl.datn.dto.request;

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
public class VoucherRequest {
    String code;
    String description;
    BigDecimal discountValue;
    BigDecimal minOrderValue;
    Integer quantity;
    Integer usageCount;
    LocalDateTime startAt;
    LocalDateTime endAt;
    Boolean isActive;
}
