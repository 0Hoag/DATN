package com.fpl.datn.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Integer id;
    LocalDateTime createdAt;
    String orderStatus;
    String paymentMethod;
    String paymentStatus;
    String note;
    // Thông tin người dùng
    String address;
    String nickname;
    String phone;
    OrderUserResponse user;
    // Thồn tin Đơn hàng chi tiết
    BigDecimal totalAmount;
    List<OrderDetailResponse> orderDetails;
    String PaymentUrl;
    // voucher
    VoucherResponse voucher;
}
