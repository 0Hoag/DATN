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

    public ProductSaleResponse(Integer productId, String name, String slug, String imageUrl,
                               BigDecimal minOriginPrice, BigDecimal minSalePrice) {
        this.productId = productId;
        this.name = name;
        this.slug = slug;
        this.imageUrl = imageUrl;
        this.minOriginPrice = minOriginPrice;
        this.minSalePrice = minSalePrice;
    }

}
