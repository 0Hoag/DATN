package com.fpl.datn.dto.response;

import com.fpl.datn.models.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    private Integer id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private Boolean isEnable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    Set<RoleResponse> roles;

    private List<Address> addresses;
    private List<Order> orders;
    private List<ProductReview> productReviews;
    private List<Cart> carts;
    private List<OrderReturn> orderReturns;
    private List<ZUserVoucher> ZUserVouchers;
    private List<ActivityLog> activityLogs;
}
