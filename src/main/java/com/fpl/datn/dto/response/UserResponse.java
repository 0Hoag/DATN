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
    LocalDateTime deletedAt;

    Set<RoleResponse> roles;

    private List<AddressResponse> addresses;
    private List<OrderResponse> orders;
    private List<ProductReviewResponse> productReviews;
    private List<CartResponse> carts;
    private List<OrderReturnResponse> orderReturns;
    private List<ZUserVoucherResponse> ZUserVouchers;
    private List<ActivityLogResponse> activityLogs;

    public boolean hasRole(String roleName) {
        if (roles == null) return false;
        return roles.stream()
                .anyMatch(role -> roleName.equals(role.getName()));
    }
}
