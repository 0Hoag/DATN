package com.fpl.datn.dto.request.Product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductImageRequest {
    String imageUrl;
    String altText;
    String specDescription;
    Boolean isThumbnail;
    int sortOrder;
    Integer productVariantId;
}
