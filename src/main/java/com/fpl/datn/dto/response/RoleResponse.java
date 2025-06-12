package com.fpl.datn.dto.response;

import com.fpl.datn.models.Permission;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

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
