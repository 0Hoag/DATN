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
public class ProductVariantRequest {
    private Integer id;
    private String variantName;
    private BigDecimal price;
    private Integer quantity;
    private Integer sold;
    private Boolean isActive;
    private Integer productId; // ID sản phẩm cha

    private List<Integer> attributeValueIds;   // 🆕 danh sách ID thuộc tính
    List<ProductImageRequest> images;
}