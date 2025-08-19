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
public class VoucherResponse {
    Integer userId;
    Integer id;
    String code;
    String description;
    Integer usageCount;
    Integer quantity;
    BigDecimal discountValue;
    BigDecimal minOrderValue;
    LocalDateTime startAt;
    LocalDateTime endAt;
    Boolean isActive;
}
