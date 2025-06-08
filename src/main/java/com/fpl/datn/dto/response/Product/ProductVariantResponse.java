package com.fpl.datn.dto.response.Product;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private List<String> productImages;
    private List<String> attributeValues;
}
