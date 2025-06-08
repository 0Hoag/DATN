package com.fpl.datn.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fpl.datn.models.*;

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

    List<Address> addresses;
    List<OrderResponse> orders;
    List<ProductReview> productReviews;
    List<Cart> carts;
    List<OrderReturn> orderReturns;
    List<ZUserVoucher> ZUserVouchers;
    List<ActivityLog> activityLogs;
}
