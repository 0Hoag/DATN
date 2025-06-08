package com.fpl.datn.dto.response.Product;

import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantAttributeResponse {
    private Integer id;
    private String name;
    private List<String> values;
}
