package com.fpl.datn.dto.response.Product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantAttributeValueResponse {
    private Integer id;
    private Integer productVariantId;
    private String attributeName; // Ví dụ: "Màu sắc"
    private String attributeValueName; // Ví dụ: "Đỏ"
}
