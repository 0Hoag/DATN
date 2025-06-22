package com.fpl.datn.dto.request.Product;

import com.fpl.datn.dto.response.Product.ProductVariantResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest {
    String name;
    String slug;
    String description;
    String brand;
    String thumbnail;
    String content;
    Boolean isHome;
    Boolean isActive;
    Integer category;
}