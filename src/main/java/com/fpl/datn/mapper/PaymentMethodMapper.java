package com.fpl.datn.mapper;

import org.mapstruct.Mapper;

import com.fpl.datn.dto.response.PaymentMethodResponse;
import com.fpl.datn.models.PaymentMethod;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {
    PaymentMethodResponse toLogResponse(PaymentMethod paymentMethod);
}
