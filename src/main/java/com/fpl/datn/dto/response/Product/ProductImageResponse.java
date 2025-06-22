package com.fpl.datn.dto.response.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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