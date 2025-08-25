package com.fpl.datn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fpl.datn.dto.response.TransactionlogResponse;
import com.fpl.datn.models.TransactionLog;

@Mapper(componentModel = "spring")
public interface TransactionLogMapper {
    @Mapping(target = "orderId", source = "order.id")
    TransactionlogResponse toLogResponse(TransactionLog transactionLog);
}
