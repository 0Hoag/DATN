package com.fpl.datn.dto.response.products;
import java.time.LocalDateTime;
import java.util.List;

import com.fpl.datn.models.Category;
import com.fpl.datn.models.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse{
    Integer id;
    String name;
    String slug;
    String description;
    Boolean isShow;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Category parent;
    List<CategoryResponse> children;
    List<Product> products;
}
