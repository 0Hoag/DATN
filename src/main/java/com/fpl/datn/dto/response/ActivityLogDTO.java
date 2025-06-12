package com.fpl.datn.dto.response;

import java.time.LocalDateTime;

public class ActivityLogDTO {
    private Integer id;
    private String actionType;
    private String tableName;
    private String description;
    private LocalDateTime createdAt;
    private String userActionName;

    // Getters & Setters
}
