package com.fpl.datn.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewResponse {
    Integer id;
    Integer rating;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isReviewed;

    // Thông tin sản phẩm
    Integer productId;
    String productName;
    String productSlug;
    String productThumbnail;

    // Thông tin người dùng
    Integer userId;
    String userFullName;
    String userEmail;

    // ID của bình luận gốc nếu đây là một bình luận trả lời
    Integer replyTo;

    // Bình luận trả lời của admin (nếu đây là bình luận gốc)
    ProductReviewResponse reply;
}
