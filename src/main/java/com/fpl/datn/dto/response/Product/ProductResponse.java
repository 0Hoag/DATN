package com.fpl.datn.dto.response.Product;

import java.sql.Timestamp;
import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Integer id;
    String name;
    String slug;
    String description;
    String brand;
    String thumbnail;
    String content;
    Boolean isHome;
    Boolean isActive;
    Timestamp createdAt;
    Timestamp updatedAt;
    List<String> productVariants;
    List<String> productReviews;
    List<String> orderDetails;
}
