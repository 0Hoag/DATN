package com.fpl.datn.dto.request.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageRequest {
    String imageUrl;
    String altText;
    String specDescription;
    Boolean isThumbnail;
    int sortOrder;
    Timestamp createAt;
}
