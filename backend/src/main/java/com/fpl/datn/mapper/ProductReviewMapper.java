package com.fpl.datn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fpl.datn.dto.response.ProductReviewResponse;
import com.fpl.datn.models.ProductReview;

@Mapper(componentModel = "spring")
public interface ProductReviewMapper {
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "producName", source = "product.name")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "thumbnail", source = "product.thumbnail")
    ProductReviewResponse toProductReviewResponse(ProductReview productReview);
}
