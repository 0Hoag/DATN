package com.fpl.datn.dto.request.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantRequest {
    private String sku;
    private String variantName;
    private BigDecimal price;
    private Integer quantity;
    private Integer sold;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer productId; // ID sản phẩm cha

    private List<String> productImages; // URL hoặc tên ảnh
    private List<String> attributeValues; // Thuộc tính biến thể
}
