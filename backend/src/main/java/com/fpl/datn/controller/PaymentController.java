package com.fpl.datn.controller;

import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpl.datn.enums.PaymentStatus;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final OrderRepository orderRepository;

    @GetMapping("/vnpay-return")
    public ResponseEntity<String> handleVnpayReturn(@RequestParam Map<String, String> params) {
        String responseCode = params.get("vnp_ResponseCode");
        String orderInfo = params.get("vnp_OrderInfo");
        int orderId = Integer.parseInt(orderInfo.replace("Order:", ""));

        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if ("00".equals(responseCode)) {
            order.setPaymentStatus(PaymentStatus.PAID.getDescription());
            //            order.setOrderStatus(OrderStatus.CONFIRMED.getDescription());
            orderRepository.save(order);
            return ResponseEntity.ok("Thanh toán thành công cho đơn hàng #" + orderId);
        } else {
            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST)
                    .body("Thanh toán thất bại cho đơn hàng #" + orderId);
        }
    }
}
