package com.fpl.datn.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZUserVoucherResponse {
    private Integer id;
    private Boolean isUsed;
    private LocalDateTime assignedAt;

    // User Info
    private Integer userId;
    private String userFullName;
    private String userEmail;

    // Voucher Info
    private Integer voucherId;
    private String voucherCode;
    private BigDecimal voucherDiscountValue;
    private BigDecimal voucherMinOrderValue;
    private LocalDateTime voucherStartAt;
    private LocalDateTime voucherEndAt;
    private Integer voucherQuantity;
    private String voucherType; // Đảm bảo trường này tồn tại
    private Boolean voucherIsActive;
}
