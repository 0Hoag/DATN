package com.fpl.datn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.fpl.datn.dto.response.CartResponse;
import com.fpl.datn.models.Cart;

@Mapper(
        componentModel = "spring",
        uses = {CartItemMapper.class})
public interface CartMapper {
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "userId", source = "user.id")
    CartResponse toCartResponse(Cart cart);
}
