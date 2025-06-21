package com.fpl.datn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.fpl.datn.dto.request.OrderStatusRequest;
import com.fpl.datn.dto.request.UpdateOrderRequest;
import com.fpl.datn.dto.response.OrderResponse;
import com.fpl.datn.models.Order;

@Mapper(
        componentModel = "spring",
        uses = {OrderDetailMapper.class})
public interface OrderMapper {

    @Mapping(target = "paymentMethod", source = "paymentMethod.name")
    @Mapping(target = "address", source = "address.addressLine")
    @Mapping(target = "PaymentUrl", ignore = true)
    OrderResponse toOrderResponse(Order order);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void toUpdateOrder(@MappingTarget Order order, UpdateOrderRequest request);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    void toUpdateStatus(@MappingTarget Order order, OrderStatusRequest request);
}
