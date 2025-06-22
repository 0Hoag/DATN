package com.fpl.datn.dto.request.Product;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    String name;

    @NotBlank(message = "Slug không được để trống")
    String slug;

    @NotBlank(message = "Miêu tả không được để trống")
    String description;

    @NotBlank(message = "Thương hiệu không được để trống")
    String brand;

    @NotBlank(message = "thumbnail không được để trống")
    String thumbnail;

    @NotBlank(message = "Nội dung không được để trống")
    String content;

    @NotNull(message = "Mục hiển thị không được để trống")
    Boolean isHome;

    @NotNull(message = "Mục hoạt động không được để trống")
    Boolean isActive;

    @NotNull(message = "Danh mục không được để trống")
    Integer category;

    List<ProductVariantRequest> productVariants;
}
