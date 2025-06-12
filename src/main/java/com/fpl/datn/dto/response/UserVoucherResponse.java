package com.fpl.datn.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVoucherResponse {
    // Thông tin UserVoucher
    private Integer id;

    // Thông tin User đầy đủ
    private Integer userId;
    private String userFullName;
    private String userEmail;
    private String userPhone;
    private Boolean userIsEnable;  // Đổi từ userIsActive sang userIsEnable để phù hợp với model

    // Thông tin Voucher đầy đủ
    private Integer voucherId;
    private String voucherCode;
    private String voucherDescription;
    private BigDecimal discountValue;
    private BigDecimal minOrderValue;
    private Integer quantity;
    private Integer usageCount;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Boolean isActive;
    private String voucherStatus; // ACTIVE, EXPIRED, INACTIVE
    private String voucherType; // PERCENT, FIXED
    private Integer remainingUses;
}
