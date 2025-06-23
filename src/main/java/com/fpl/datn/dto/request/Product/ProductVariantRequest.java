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
public class ProductVariantRequest {
    
    private Integer id;

    @NotBlank(message = "PRODUCT_VARIANT_NAME_REQUIRED")
    private String variantName;

    @NotNull(message = "PRODUCT_VARIANT_PRICE_REQUIRED")
    private BigDecimal price;

    @NotNull(message = "PRODUCT_VARIANT_QUANTITY_REQUIRED")
    private Integer quantity;

    @NotNull(message = "PRODUCT_VARIANT_SOLD_REQUIRED")
    private Integer sold;

    @NotNull(message = "PRODUCT_VARIANT_ISACTIVE_REQUIRED")
    private Boolean isActive;

    @NotNull(message = "PRODUCT_VARIANT_PRODUCT_ID_REQUIRED")
    private Integer productId;

    @NotNull(message = "PRODUCT_VARIANT_ATTRIBUTE_VALUE_IDS_REQUIRED")
    private List<Integer> attributeValueIds;

    private List<ProductImageRequest> images;
}
