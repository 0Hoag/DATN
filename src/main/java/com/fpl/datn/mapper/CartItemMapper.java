package com.fpl.datn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.fpl.datn.dto.response.CartItemResponse;
import com.fpl.datn.models.CartItem;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    CartItemResponse toCartItemResponse(CartItem cartItem);
}
