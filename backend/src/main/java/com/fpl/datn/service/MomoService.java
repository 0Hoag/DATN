package com.fpl.datn.service;

import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jakarta.xml.bind.DatatypeConverter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpl.datn.configuration.MomoConfig;
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
public class MomoService {
    MomoConfig config;
    OrderRepository orderRepository;
    TransactionLogService logService;

    public PaymentResponse createMomoPayment(Order order) throws JsonProcessingException {
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = order.getId().toString();
        String amount = order.getTotalAmount().toBigInteger().toString();
        String orderInfo = "Thanh toán đơn hàng #" + orderId;

        // rawData không chứa secretKey
        String rawData = "accessKey=" + config.getAccessKey()
                + "&amount=" + amount
                + "&extraData="
                + "&ipnUrl=" + config.getNotifyUrl()
                + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo
                + "&partnerCode=" + config.getPartnerCode()
                + "&redirectUrl=" + config.getReturnUrl()
                + "&requestId=" + requestId
                + "&requestType=captureWallet";

        String signature = hmacSHA256(rawData, config.getSecretKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", config.getPartnerCode());
        requestBody.put("accessKey", config.getAccessKey());
        requestBody.put("requestId", requestId);
        requestBody.put("amount", amount);
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", orderInfo);
        requestBody.put("redirectUrl", config.getReturnUrl());
        requestBody.put("ipnUrl", config.getNotifyUrl());
        requestBody.put("extraData", "");
        requestBody.put("requestType", "captureWallet");
        requestBody.put("lang", "vi");
        requestBody.put("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestBody, headers);

        String returnUrl = config.getUrl();
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.postForObject(returnUrl, httpEntity, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(response, Map.class);
        String payUrl = responseMap.get("payUrl").toString();

        return PaymentResponse.builder().txnRef(orderId).paymentUrl(payUrl).build();
    }

    public String handleMomoReturn(Map<String, String> params) {
        try {
            int orderId = Integer.parseInt(params.get("orderId"));
            String responseCode = params.get("resultCode");
            String signature = params.get("signature");
            String txnRef = params.get("orderId");
            String transactionNo = params.get("transId");

            // Rebuild rawData để xác minh chữ ký
            String rawData = "accessKey=" + config.getAccessKey()
                    + "&amount=" + params.get("amount")
                    + "&extraData=" + params.get("extraData")
                    + "&orderId=" + params.get("orderId")
                    + "&orderInfo=" + params.get("orderInfo")
                    + "&orderType=" + params.get("orderType")
                    + "&partnerCode=" + params.get("partnerCode")
                    + "&payType=" + params.get("payType")
                    + "&requestId=" + params.get("requestId")
                    + "&responseTime=" + params.get("responseTime")
                    + "&resultCode=" + params.get("resultCode")
                    + "&message=" + params.get("message")
                    + "&transId=" + params.get("transId");

            String expectedSignature = hmacSHA256(rawData, config.getSecretKey());

            if (!expectedSignature.equals(signature)) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            var order =
                    orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

            if ("00".equals(responseCode)) {
                order.setPaymentStatus(PaymentStatus.PAID.getDescription());
                orderRepository.save(order);
                logService.logPayment(order, OrderActionType.PAYMENT_SUCCESS.getType(), txnRef, transactionNo);
                return "Thanh toán thành công cho đơn hàng #" + orderId;
            } else {
                return "Thanh toán thất bại cho đơn hàng #" + orderId;
            }
        } catch (Exception e) {
            return "Đã xảy ra lỗi xử lý thanh toán: " + e.getMessage();
        }
    }

    public String hmacSHA256(String data, String key) {
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            hmacSha256.init(secretKeySpec);
            byte[] hash = hmacSha256.doFinal(data.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(hash).toLowerCase();
        } catch (Exception ex) {
            throw new RuntimeException("Error generating HMAC SHA256", ex);
        }
    }
}
