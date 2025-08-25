package com.fpl.datn.dto.response;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityLogResponse {
    private Integer id;
    private String action;
    private String description;
    private String module;
    private Integer objectID;
    private Integer userId;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
