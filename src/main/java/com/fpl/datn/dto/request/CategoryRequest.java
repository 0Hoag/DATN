package com.fpl.datn.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {
    String name;
    String slug;
    String description;
    Boolean isShow;
    // Relationships - Self-referencing for parent-child categories
    int parent;
}
