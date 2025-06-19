package com.fpl.datn.dto.request.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductVariantAttributeValueRequest {
    @NotNull(message = "ID biến thể không được để trống")
    private Integer productVariantId;

    @NotNull(message = "ID thuộc tính không được để trống")
    private Integer attributeValueId;
}
