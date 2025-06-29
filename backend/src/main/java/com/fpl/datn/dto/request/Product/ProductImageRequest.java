package com.fpl.datn.dto.request.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageRequest {
    Integer id;

    @NotBlank(message = "PRODUCT_IMAGE_ALT_TEXT_REQUIRED")
    String altText;

    @NotBlank(message = "PRODUCT_IMAGE_DESCRIPTION_REQUIRED")
    String specDescription;

    @NotNull(message = "PRODUCT_IMAGE_IS_THUMBNAIL_REQUIRED")
    Boolean isThumbnail;

    @NotNull(message = "PRODUCT_IMAGE_SORT_ORDER_REQUIRED")
    Integer sortOrder;

    @NotNull(message = "PRODUCT_IMAGE_VARIANT_ID_REQUIRED")
    Integer productVariantId;

    @NotNull(message = "PRODUCT_IMAGE_URL_REQUIRED")
    String imageUrl;
}
