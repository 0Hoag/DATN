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
    private String sku;
    private String variantName;
    private BigDecimal price;
    private Integer quantity;
    private Integer sold;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer productId;
    private List<ProductImageResponse> productImages;
    private List<ProductVariantAttributeValueResponse> attributeValues;
}
