package com.fpl.datn.dto.request.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductRequest {
    @NotBlank(message = "PRODUCT_NAME_REQUIRED")
    String name;

    @NotBlank(message = "PRODUCT_SLUG_REQUIRED")
    String slug;

    @NotBlank(message = "PRODUCT_DESCRIPTION_REQUIRED")
    String description;

    @NotBlank(message = "PRODUCT_BRAND_REQUIRED")
    String brand;

    @NotBlank(message = "PRODUCT_THUMBNAIL_REQUIRED")
    String thumbnail;

    @NotBlank(message = "PRODUCT_CONTENT_REQUIRED")
    String content;

    @NotNull(message = "PRODUCT_ISHOME_REQUIRED")
    Boolean isHome;

    @NotNull(message = "PRODUCT_ISACTIVE_REQUIRED")
    Boolean isActive;

    @NotNull(message = "PRODUCT_CATEGORY_REQUIRED")
    Integer category;
}
