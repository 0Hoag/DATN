package com.fpl.datn.dto.request.Product;

import java.math.BigDecimal;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
