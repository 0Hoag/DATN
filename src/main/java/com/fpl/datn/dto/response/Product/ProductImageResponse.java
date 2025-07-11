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
    Integer id;
    String imageUrl;
    String altText;
    String specDescription;
    Boolean isThumbnail;
    Integer sortOrder;
    Timestamp createdAt;
    Timestamp updatedAt;
    Integer productVariantId;
}
