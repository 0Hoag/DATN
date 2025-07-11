package com.fpl.datn.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewStatsResponse {
    private Integer productId;
    private String productName;
    private Double averageRating;
    private Long totalReviews;
    private Map<Integer, Long> ratingDistribution; // {1: 5, 2: 10, 3: 20, 4: 30, 5: 35}
}
