package com.fpl.datn.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    String name;
    String displayName;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
