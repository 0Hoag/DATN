package com.fpl.datn.dto.response.Product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantAttributeValueResponse {
    private Integer id;
    private String attributeName;
    private String attributeValue;
    private String productVariantName;
}
