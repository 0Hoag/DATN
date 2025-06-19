package com.fpl.datn.dto.request.Product;

import java.math.BigDecimal;
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
public class UpdateProductVariantRequest {
    Integer id;
    private String variantName;
    private BigDecimal price;
    private Integer quantity;
    private Integer sold;
    private Boolean isActive;
    private Integer productId; // ID sản phẩm cha

    private List<Integer> attributeValueIds; // 🆕 danh sách ID thuộc tính
    List<ProductImageRequest> images;
}
