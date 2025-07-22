package com.fpl.datn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.fpl.datn.dto.response.ProductReviewResponse;
import com.fpl.datn.models.ProductReview;

@Mapper(componentModel = "spring")
public interface ProductReviewMapper {

    ProductReviewMapper INSTANCE = Mappers.getMapper(ProductReviewMapper.class);

    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "userFullName", source = "user.fullName")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productThumbnail", source = "product.thumbnail")
    @Mapping(target = "replyTo", source = "parentReview.id") // Ánh xạ ID của bình luận gốc
    @Mapping(target = "reply", source = "adminReply", qualifiedByName = "mapAdminReply") // Ánh xạ bình luận trả lời
    ProductReviewResponse toProductReviewResponse(ProductReview productReview);

    @Named("mapAdminReply")
    default ProductReviewResponse mapAdminReply(ProductReview adminReply) {
        if (adminReply == null) {
            return null;
        }
        return ProductReviewResponse.builder()
                .id(adminReply.getId())
                .rating(adminReply.getRating()) // Sẽ là null cho admin reply
                .content(adminReply.getContent())
                .createdAt(adminReply.getCreatedAt())
                .updatedAt(adminReply.getUpdatedAt())
                .productId(
                        adminReply.getProduct() != null
                                ? adminReply.getProduct().getId()
                                : null)
                .productName(
                        adminReply.getProduct() != null
                                ? adminReply.getProduct().getName()
                                : null)
                .productThumbnail(
                        adminReply.getProduct() != null
                                ? adminReply.getProduct().getThumbnail()
                                : null)
                .userId(adminReply.getUser() != null ? adminReply.getUser().getId() : null)
                .userFullName(
                        adminReply.getUser() != null ? adminReply.getUser().getFullName() : null)
                .userEmail(adminReply.getUser() != null ? adminReply.getUser().getEmail() : null)
                .replyTo(
                        adminReply.getParentReview() != null
                                ? adminReply.getParentReview().getId()
                                : null) // Ánh xạ ID của bình luận gốc
                .build();
    }
}
