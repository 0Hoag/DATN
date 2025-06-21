package com.fpl.datn.dto.request.Product;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    Integer productVariantId;
    MultipartFile image;
}
