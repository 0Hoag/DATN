package com.fpl.datn.dto.response.Product;

import java.time.LocalDateTime;
import java.util.List;

import com.fpl.datn.models.Order;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponse {
    private Integer id;
    private String fullName;
    private String phone;
    private String addressLine;
    private Boolean isDefault;
    private Integer userID;
    private List<Order> orders;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
