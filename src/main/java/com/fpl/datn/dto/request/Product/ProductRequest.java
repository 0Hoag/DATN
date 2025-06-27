package com.fpl.datn.dto.request.Product;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
    String name;
    String slug;
    String description;
    String brand;
    String thumbnail;
    String content;
    Boolean isHome;
    Boolean isActive;
    Integer category;
    List<String> productVariants;
    List<String> productReviews;
    List<String> orderDetails;
}
