package com.fpl.datn.dto.request.Product;

import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductImageRequest {
    @NotNull(message = "PRODUCT_IMAGE_ID_REQUIRED")
    Integer id;

    String altText;

    String specDescription;

    @NotNull(message = "PRODUCT_IMAGE_IS_THUMBNAIL_REQUIRED")
    Boolean isThumbnail;

    @NotNull(message = "PRODUCT_IMAGE_SORT_ORDER_REQUIRED")
    Integer sortOrder;

    @NotNull(message = "PRODUCT_IMAGE_UPLOAD_ID_REQUIRED")
    Integer imageUrl;
}
