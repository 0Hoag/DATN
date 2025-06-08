package com.fpl.datn.dto.request.Product;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantAttributeRequest {
    private String name;
    private List<String> values; // danh sách các giá trị như ["Đỏ", "Xanh", "Đen"]
}
