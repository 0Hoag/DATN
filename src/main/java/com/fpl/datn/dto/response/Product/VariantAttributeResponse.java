package com.fpl.datn.dto.response.Product;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantAttributeResponse {
    private Integer id;
    private String name;
    private List<VariantAttributeValueResponse> values;
}