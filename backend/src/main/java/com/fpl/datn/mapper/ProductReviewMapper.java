package com.fpl.datn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fpl.datn.dto.response.ProductReviewResponse;
import com.fpl.datn.models.ProductReview;

@Mapper(componentModel = "spring")
public interface ProductReviewMapper {

    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userFullName", source = "user.fullName") // Ánh xạ tên đầy đủ của người dùng vào userFullName
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productName", source = "product.name") // Ánh xạ tên sản phẩm vào productName
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productSlug", source = "product.slug")
    @Mapping(target = "productThumbnail", source = "product.thumbnail")
    @Mapping(target = "isReviewed", source = "orderDetail.isReviewed")
    ProductReviewResponse toProductReviewResponse(ProductReview productReview);
}
