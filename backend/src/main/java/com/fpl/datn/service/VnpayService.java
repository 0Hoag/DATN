package com.fpl.datn.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpl.datn.configuration.VnpayConfig;
import com.fpl.datn.dto.response.PaymentResponse;
import com.fpl.datn.enums.OrderActionType;
import com.fpl.datn.enums.PaymentStatus;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.models.Order;
import com.fpl.datn.models.OrderReturn;
import com.fpl.datn.repository.OrderRepository;
import com.fpl.datn.repository.TransactionLogRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VnpayService {
    OrderRepository orderRepository;
    TransactionLogRepository logRepository;
    TransactionLogService logService;
    VnpayConfig config;

    public PaymentResponse createPaymentUrl(Order order, HttpServletRequest request) {
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = request.getRemoteAddr();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", config.getTmnCode());
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

        // xây dựng truy vấn với băm bảo mật
        String query = buildQueryWithSecureHash(vnp_Params);

        String paymentUrl = config.getUrl() + "?" + query.toString();
        var response = PaymentResponse.builder()
                .txnRef(vnp_TxnRef)
                .paymentUrl(paymentUrl)
                .build();
        return response;
    }

    public String refund(OrderReturn orderReturn, String createBy, HttpServletRequest request)
            throws JsonMappingException, JsonProcessingException {

        var transactionLog = logRepository.findFirstByOrderIdAndActionTypeOrderByCreatedAtDesc(
                orderReturn.getOrder().getId(), OrderActionType.PAYMENT_SUCCESS.getType());
        if (transactionLog == null) throw new AppException(ErrorCode.TRANCSACTION_LOG_NOT_FOUND);
        String txnRef = transactionLog.getTransactionRef();
        String transactionNo = transactionLog.getTransactionNo();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_RequestId", UUID.randomUUID().toString());
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "refund");
        vnp_Params.put("vnp_TmnCode", config.getTmnCode());
        vnp_Params.put("vnp_TransactionType", "02"); // 02 = Hoàn tiền toàn bộ
        vnp_Params.put("vnp_TxnRef", txnRef);
        vnp_Params.put("vnp_TransactionNo", transactionNo);
        // BigDecimal vnpAmount =
        // orderReturn.getRefundAmount().multiply(BigDecimal.valueOf(100));
        // vnp_Params.put("vnp_Amount", vnpAmount.toBigInteger().toString());
        BigDecimal vnpAmount =
                orderReturn.getRefundAmount().multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP);
        vnp_Params.put("vnp_Amount", vnpAmount.toPlainString());
        vnp_Params.put("vnp_OrderInfo", "Order Refund:" + orderReturn.getOrder().getId());
        vnp_Params.put("vnp_CreateBy", createBy);
        vnp_Params.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        vnp_Params.put("vnp_IpAddr", request.getRemoteAddr());

        // xây dựng truy vấn với băm bảo mật
        String query = buildQueryWithSecureHash(vnp_Params);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> body = new HttpEntity<>(query.toString(), headers);

        String refundUrl = config.getRefundUrl(); // Cấu hình URL riêng cho refund
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.postForObject(refundUrl, body, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(response, Map.class);
        String responseCode = (String) responseMap.get("vnp_ResponseCode");
        String message = (String) responseMap.get("vnp_Message");

        // Xử lý kết quả hoàn tiền
        if ("00".equals(responseCode)) {
            // Hoàn tiền thành công
            logService.logReturn(
                    orderReturn,
                    OrderActionType.REFUND_SUCCESS.getType(),
                    txnRef,
                    transactionNo,
                    orderReturn.getOrder());
        } else {
            // Hoàn tiền thất bại
            logService.logReturn(
                    orderReturn,
                    OrderActionType.REFUND_FAILED.getType(),
                    txnRef,
                    transactionNo,
                    orderReturn.getOrder());
            throw new RuntimeException("Hoàn tiền thất bại từ VNPAY: " + message);
        }
        return response;
    }

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
            logService.logPayment(order, OrderActionType.PAYMENT_SUCCESS.getType(), txnRef, transactionNo);
            return "Thanh toán thành công cho đơn hàng #" + orderId;
        } else {
            return "Thanh toán thất bại cho đơn hàng #" + orderId;
        }
    }

    // xử lí truy vấn với băm bảo mật
    private String buildQueryWithSecureHash(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder query = new StringBuilder();
        StringBuilder hashData = new StringBuilder();

        for (String name : fieldNames) {
            String value = params.get(name);
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
        System.out.println("Raw hash string: " + rawHash);
        System.out.println("Secure hash: " + secureHash);
        query.append("vnp_SecureHash=").append(secureHash);
        return query.toString();
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
}
