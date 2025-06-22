package com.fpl.datn.dto.response.Product;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantResponse {
    private Integer id;
    private String variantName;
    private Long price;
    private Integer quantity;
    private Integer sold;
    private Boolean isActive;
    private List<ProductVariantAttributeValueResponse> attributeValues;
    private List<ProductImageResponse> images;
    private Integer productId;
}
