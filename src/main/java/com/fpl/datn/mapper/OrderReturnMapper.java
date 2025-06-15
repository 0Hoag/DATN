package com.fpl.datn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.fpl.datn.dto.request.OrderReturnStatusRequest;
import com.fpl.datn.dto.response.OrderReturnResponse;
import com.fpl.datn.models.OrderReturn;

@Mapper(
        componentModel = "spring",
        uses = {OrderMapper.class})
public interface OrderReturnMapper {
    @Mapping(target = "fullName", source = "user.fullName")
    OrderReturnResponse toOrderReturnResponse(OrderReturn orderReturn);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "reason", ignore = true)
    void toUpdateOrderStatus(@MappingTarget OrderReturn orderReturn, OrderReturnStatusRequest request);
}
