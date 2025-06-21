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

    public void logPayment(Order order, String message, String transactionRef) {
        var orderlog = TransactionLog.builder()
                .amount(order.getTotalAmount())
                .createdAt(LocalDateTime.now())
                .message(message + " order " + order.getId())
                .transactionRef(transactionRef)
                .status(order.getOrderStatus())
                .type(1) // 1 = payment
                .order(order)
                .paymentMethod(order.getPaymentMethod())
                .build();
        repository.save(orderlog);
    }

    public void logReturn(OrderReturn orderReturn, String message) {
        var paymentMethod = methodRepository
                .findById(Integer.valueOf(orderReturn.getPaymentMethod()))
                .orElse(null);
        var orderlog = TransactionLog.builder()
                .amount(orderReturn.getRefundAmount())
                .createdAt(LocalDateTime.now())
                .message(message + " order return " + orderReturn.getId())
                .status(orderReturn.getStatus())
                .type(2) // 2 Return
                .orderReturn(orderReturn)
                .paymentMethod(paymentMethod)
                .build();
        repository.save(orderlog);
    }
}
