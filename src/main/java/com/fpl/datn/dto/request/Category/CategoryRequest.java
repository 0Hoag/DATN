package com.fpl.datn.dto.request.Category;
import java.time.LocalDateTime;

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
    String parent; // lưu parent dưới dạng tên hoặc slug (tùy bạn định nghĩa)
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
