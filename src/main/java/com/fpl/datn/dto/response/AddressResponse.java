package com.fpl.datn.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResponse {
    Integer id;
    String fullName;
    String phone;
    String addressLine;
    Boolean isDefault;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Integer userId;
}
