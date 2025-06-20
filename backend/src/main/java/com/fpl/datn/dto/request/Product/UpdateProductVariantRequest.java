package com.fpl.datn.dto.request.Product;

import java.math.BigDecimal;
import java.util.List;

import com.fpl.datn.dto.response.Product.VariantAttributeResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
