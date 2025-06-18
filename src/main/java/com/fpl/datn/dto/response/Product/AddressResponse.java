package com.fpl.datn.dto.response.Product;

import com.fpl.datn.models.Order;
import com.fpl.datn.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

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
