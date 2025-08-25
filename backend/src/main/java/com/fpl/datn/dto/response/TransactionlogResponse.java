package com.fpl.datn.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionlogResponse {
    Integer id;
    String status;
    Double amount;
    String actionType;
    Integer orderId;
    String message;
    LocalDateTime createdAt;
    //    String transactionRef;
    //    String transactionNo;
}
