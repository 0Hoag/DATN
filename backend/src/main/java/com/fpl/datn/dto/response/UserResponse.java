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

    List<AddressResponse> addresses;
    List<OrderResponse> orders;
    List<ProductReviewResponse> productReviews;
    List<CartResponse> carts;
    List<OrderReturnResponse> orderReturns;
    List<UserVoucherResponse> ZUserVouchers;
    List<ActivityLogResponse> activityLogs;
}
