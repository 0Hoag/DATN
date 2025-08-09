package com.fpl.datn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fpl.datn.dto.response.OrderDetailResponse;
import com.fpl.datn.models.OrderDetail;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "variantName", source = "productVariant.variantName")
    @Mapping(target = "imageUrl", expression = "java(getFirstImageUrl(orderDetail))")
    OrderDetailResponse toDetailResponse(OrderDetail orderDetail);

    default String getFirstImageUrl(OrderDetail orderDetail) {
        if (orderDetail.getProductVariant() != null
                && orderDetail.getProductVariant().getProductImages() != null
                && !orderDetail.getProductVariant().getProductImages().isEmpty()) {
            return orderDetail.getProductVariant().getProductImages().get(0).getImageUrl();
        }
        return null;
    }
}
