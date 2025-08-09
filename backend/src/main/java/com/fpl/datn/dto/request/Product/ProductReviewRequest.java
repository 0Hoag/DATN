package com.fpl.datn.dto.request.Product;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewRequest {
    private Integer productId;
    private Integer userId;
    private Integer rating;
    private String content;
}
