package com.fpl.datn.dto.request.Product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttributeValueRequest {
    private Integer attributeId; // ID của thuộc tính cha (ví dụ: Color)
    private String value; // Giá trị cụ thể (ví dụ: Red)
}
