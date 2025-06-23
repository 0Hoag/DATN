package com.fpl.datn.service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

import com.fpl.datn.configuration.VnpayConfig;
import com.fpl.datn.dto.response.PaymentResponse;
import com.fpl.datn.enums.OrderActionType;
import com.fpl.datn.enums.PaymentStatus;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.models.Order;
import com.fpl.datn.repository.OrderRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VnpayService {
    TransactionLogService logService;
    OrderRepository orderRepository;
    VnpayConfig config;

    public PaymentResponse createPaymentUrl(Order order, HttpServletRequest request) {
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = request.getRemoteAddr();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", config.getTmnCode());
        //        BigDecimal amount =
        //                order.getTotalAmount().multiply(BigDecimal.valueOf(1000)).setScale(0, RoundingMode.DOWN);

        BigDecimal vnpAmount = order.getTotalAmount().multiply(BigDecimal.valueOf(100));
        vnp_Params.put("vnp_Amount", vnpAmount.toBigInteger().toString());
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Order:" + order.getId());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", config.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String name : fieldNames) {
            String value = vnp_Params.get(name);
            hashData.append(name)
                    .append('=')
                    .append(URLEncoder.encode(value, StandardCharsets.US_ASCII))
                    .append('&');
            query.append(name)
                    .append('=')
                    .append(URLEncoder.encode(value, StandardCharsets.US_ASCII))
                    .append('&');
        }

        String rawHash = hashData.substring(0, hashData.length() - 1);
        String secureHash = hmacSHA512(config.getHashSecret(), rawHash);
        query.append("vnp_SecureHash=").append(secureHash);

        String paymentUrl = config.getUrl() + "?" + query.toString();
        var response = PaymentResponse.builder()
                .txnRef(vnp_TxnRef)
                .paymentUrl(paymentUrl)
                .build();
        return response;
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac.init(secretKey);
            return DatatypeConverter.printHexBinary(hmac.doFinal(data.getBytes()))
                    .toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo secure hash", e);
        }
    }

    //    public String extractTxnRefFromUrl(String url) {
    //        try {
    //            String[] parts = url.split("[?&]");
    //            for (String part : parts) {
    //                if (part.startsWith("vnp_TxnRef=")) {
    //                    return part.split("=")[1];
    //                }
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return null;
    //    }

    public String handleVnpayReturn(Map<String, String> params) {
        String responseCode = params.get("vnp_ResponseCode");
        String orderInfo = params.get("vnp_OrderInfo");
        String txnRef = params.get("vnp_TxnRef");
        String transactionNo = params.get("vnp_TransactionNo");
        int orderId = Integer.parseInt(orderInfo.replace("Order:", ""));

        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if ("00".equals(responseCode)) {
            order.setPaymentStatus(PaymentStatus.PAID.getDescription());
            orderRepository.save(order);
            logService.logPayment(order, OrderActionType.VNPAY_SUCCESS.getType(), txnRef, transactionNo);
            return "Thanh toán thành công cho đơn hàng #" + orderId;
        } else {
            return "Thanh toán thất bại cho đơn hàng #" + orderId;
        }
    }
}
