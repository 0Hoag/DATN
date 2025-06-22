package com.fpl.datn.dto.request.Product;

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
public class UpdateProductVariantRequest {
    private Integer id;
    private Integer productId;
    private String variantName;
    private BigDecimal price;
    private Integer quantity;
    private Integer sold;
    private Boolean isActive;
    private List<Integer> attributeValueIds;
    private List<ProductImageRequest> images;
}