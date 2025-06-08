package com.fpl.datn.dto.response.Product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttributeValueResponse {
    private Integer id;
    private String value;
    private String attributeName;
}
