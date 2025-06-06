package com.fpl.datn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.fpl.datn.dto.request.UpdateOrderRequest;
import com.fpl.datn.dto.response.OrderResponse;
import com.fpl.datn.models.Order;

@Mapper(componentModel = "Spring")
public interface OrderMapper {

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "paymentMethod", ignore = true)
    OrderResponse toOrderResponse(Order order);

    @BeanMapping(ignoreByDefault = true)
    void toUpdateOrder(@MappingTarget Order order, UpdateOrderRequest request);
}
