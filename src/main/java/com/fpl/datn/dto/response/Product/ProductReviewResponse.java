package com.fpl.datn.dto.response.Product;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewResponse {
    private Integer id;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private String userFullName; // Có thể lấy từ user.getFullName()
}
