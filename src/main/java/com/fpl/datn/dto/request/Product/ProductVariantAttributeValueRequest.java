package com.fpl.datn.dto.request.Product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantAttributeValueRequest {
    private Integer productVariantId;
    private Integer attributeValueId;
}
