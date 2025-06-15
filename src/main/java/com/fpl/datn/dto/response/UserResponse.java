package com.fpl.datn.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Integer id;
    String fullName;
    String email;
    String password;
    String phone;
    Boolean isEnable;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    Set<RoleResponse> roles;

    private List<AddressResponse> addresses;
    private List<OrderResponse> orders;
    private List<ProductReviewResponse> productReviews;
    private List<CartResponse> carts;
    private List<OrderReturnResponse> orderReturns;
    private List<ZUserVoucherResponse> ZUserVouchers;
    private List<ActivityLogResponse> activityLogs;
}
