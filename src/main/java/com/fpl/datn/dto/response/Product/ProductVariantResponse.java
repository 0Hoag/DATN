package com.fpl.datn.dto.response.Product;

import com.fpl.datn.mapper.Product.ProductImageMapper;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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