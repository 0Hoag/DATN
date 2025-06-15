package com.fpl.datn.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductReviewResponse {
    // user
    String fullName;
    String email;

    // product
    int productId;
    String producName;
    String thumbnail;

    Integer rating;
    String content;
}
