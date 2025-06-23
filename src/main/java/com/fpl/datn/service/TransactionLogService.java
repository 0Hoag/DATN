package com.fpl.datn.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fpl.datn.models.Order;
import com.fpl.datn.models.OrderReturn;
import com.fpl.datn.models.TransactionLog;
import com.fpl.datn.repository.PaymentMethodRepository;
import com.fpl.datn.repository.TransactionLogRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionLogService {
    TransactionLogRepository repository;
    PaymentMethodRepository methodRepository;

    public List<TransactionLog> Get() {
        return repository.findAll();
    }

    public void logPayment(Order order, String acctionType, String transactionRef, String transactionNo) {
        var orderlog = TransactionLog.builder()
                .amount(order.getTotalAmount())
                .createdAt(LocalDateTime.now())
                .message(acctionType + " order " + order.getId())
                .transactionRef(transactionRef)
                .transactionNo(transactionNo)
                .status(order.getOrderStatus())
                .type(1) // 1 = payment
                .actionType(acctionType)
                .order(order)
                .paymentMethod(order.getPaymentMethod())
                .build();
        repository.save(orderlog);
    }

    public void logReturn(OrderReturn orderReturn, String acctionType) {
        var paymentMethod = methodRepository
                .findById(Integer.valueOf(orderReturn.getPaymentMethod()))
                .orElse(null);
        var orderlog = TransactionLog.builder()
                .amount(orderReturn.getRefundAmount())
                .createdAt(LocalDateTime.now())
                .message(acctionType + " order return " + orderReturn.getId())
                .actionType(acctionType)
                .status(orderReturn.getStatus())
                .type(2) // 2 Return
                .orderReturn(orderReturn)
                .paymentMethod(paymentMethod)
                .build();
        repository.save(orderlog);
    }
}
