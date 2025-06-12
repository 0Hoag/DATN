package com.fpl.datn.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer userActionId;
}
