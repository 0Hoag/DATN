package com.fpl.datn.dto.response.Product;

import java.sql.Timestamp;
import java.util.List;

import com.fpl.datn.dto.response.CategoryResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Integer id;
    String name;
    String description;
    String brand;
    String content;
    String thumbnail;
    Timestamp createdAt;
    Boolean isHome;
    Boolean isActive;
    List<ProductVariantResponse> productVariants;
    CategoryResponse category;
}
