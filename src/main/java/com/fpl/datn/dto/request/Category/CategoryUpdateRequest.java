package com.fpl.datn.dto.request.Category;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryUpdateRequest {
    String name;
    String slug;
    String description;
    Boolean isShow;
    String parent;
    LocalDateTime updatedAt;
}
