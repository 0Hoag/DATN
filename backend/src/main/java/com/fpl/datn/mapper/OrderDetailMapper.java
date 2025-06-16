package com.fpl.datn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fpl.datn.dto.response.OrderDetailResponse;
import com.fpl.datn.models.OrderDetail;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {

    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "variantName", source = "productVariant.variantName")
    OrderDetailResponse toDetailResponse(OrderDetail orderDetail);
}
