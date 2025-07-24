package com.fpl.datn.dto.response.Product;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSaleResponse {
    private Integer productId;
    private String name;
    private String slug;
    private BigDecimal minOriginPrice;
    private BigDecimal minSalePrice;
    private String imageUrl;
    private Double averageRating;

}
