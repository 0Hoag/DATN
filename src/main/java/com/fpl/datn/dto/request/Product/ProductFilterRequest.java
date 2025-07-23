package com.fpl.datn.dto.request.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductFilterRequest {
    private Integer categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
