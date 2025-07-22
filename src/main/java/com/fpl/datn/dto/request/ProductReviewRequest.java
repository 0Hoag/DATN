package com.fpl.datn.dto.request;

import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewRequest {
    // Các annotation validation đã được loại bỏ để cho phép null khi là bình luận trả lời
    private Integer productId;
    private Integer rating;

    @NotBlank(message = "Nội dung đánh giá không được để trống")
    @Size(min = 10, max = 1000, message = "Nội dung đánh giá phải từ 10-1000 ký tự")
    private String content;

    private Integer replyTo; // Trường này vẫn được giữ lại cho chức năng trả lời
}
