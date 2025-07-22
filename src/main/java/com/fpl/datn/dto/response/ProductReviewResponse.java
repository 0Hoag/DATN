package com.fpl.datn.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewResponse {
    private Integer id;
    private Integer rating;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Thông tin sản phẩm
    private Integer productId;
    private String productName;
    private String productThumbnail;

    // Thông tin người dùng
    private Integer userId;
    private String userFullName;
    private String userEmail;

    // ID của bình luận gốc nếu đây là một bình luận trả lời
    private Integer replyTo;

    // Bình luận trả lời của admin (nếu đây là bình luận gốc)
    private ProductReviewResponse reply;
}
