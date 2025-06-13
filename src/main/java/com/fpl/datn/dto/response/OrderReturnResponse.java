package com.fpl.datn.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderReturnResponse {
    private Integer id;
    private String reason;
    private BigDecimal refundAmount;
    private Integer paymentMethod;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer userId;
    private Integer orderId;
}
