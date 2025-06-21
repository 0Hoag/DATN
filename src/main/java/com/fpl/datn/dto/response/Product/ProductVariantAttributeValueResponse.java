package com.fpl.datn.dto.response.Product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantAttributeValueResponse {
    private Integer id;
    private Integer productVariantId;
    private Integer attributeValueId;
    private String attributeValue; // VD: "Xanh"
    private String attributeName; // VD: "Màu sắc"
}
