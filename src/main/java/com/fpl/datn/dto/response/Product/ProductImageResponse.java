package com.fpl.datn.dto.response.Product;

import java.sql.Timestamp;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageResponse {
    int id;
    String imageUrl;
    String altText;
    String specDescription;
    Boolean isThumbnail;
    int sortOrder;
    Timestamp createdAt;
    Timestamp updatedAt;
    Integer productVariantId;
}
