package com.fpl.datn.mapper;

import org.mapstruct.Mapper;

import com.fpl.datn.dto.response.TransactionlogResponse;
import com.fpl.datn.models.TransactionLog;

@Mapper(componentModel = "spring")
public interface TransactionLogMapper {
    TransactionlogResponse toLogResponse(TransactionLog transactionLog);
}
