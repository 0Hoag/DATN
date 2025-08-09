package com.fpl.datn.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String name;
    String displayName;
    String description;
    Set<String> permissions;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
