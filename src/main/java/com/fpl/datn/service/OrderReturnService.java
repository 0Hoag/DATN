package com.fpl.datn.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.OrderReturnRequest;
import com.fpl.datn.dto.request.OrderReturnStatusRequest;
import com.fpl.datn.dto.response.OrderReturnResponse;
import com.fpl.datn.enums.OrderActionType;
import com.fpl.datn.enums.OrderReturnEnums;
import com.fpl.datn.enums.OrderStatus;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.OrderReturnMapper;
import com.fpl.datn.models.OrderReturn;
import com.fpl.datn.repository.OrderRepository;
import com.fpl.datn.repository.OrderReturnRepository;
import com.fpl.datn.repository.TransactionLogRepository;
import com.fpl.datn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderReturnService {

    //    VnpayService vnpayService;
    OrderReturnRepository repository;
    OrderRepository orderRepository;
    UserRepository userRepository;
    TransactionLogRepository logRepository;
    OrderReturnMapper mapper;
    TransactionLogService logService;

    public PageResponse<OrderReturnResponse> get(int page, int size, boolean isDesc) {
        Sort sort = isDesc ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = repository.findAll(pageable);
        var data = pageData.stream()
                .map(orderReturn -> mapper.toOrderReturnResponse(orderReturn))
                .toList();
        return PageResponse.<OrderReturnResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    @Transactional
    public OrderReturnResponse create(OrderReturnRequest request) {
        var order = orderRepository
                .findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        var user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Chỉ cho trả hàng nếu đã nhận hàng
        if (!order.getOrderStatus().equalsIgnoreCase(OrderStatus.RECEIED.getDescription()))
            throw new AppException(ErrorCode.INVALID_ORDER_STATUS);

        // Chỉ cho trả hàng trong vòng 7 ngày sau khi giao
        if (Duration.between(order.getUpdatedAt(), LocalDateTime.now()).toDays() > 7)
            throw new AppException(ErrorCode.RETURN_PERIOD_EXPIRED);

        // Chỉ cho trả lại nếu chưa trả hoặc đã bị từ chối
        if (repository.existsByOrderAndStatusNot(order, OrderReturnEnums.REJECTED.getDescription())) {
            throw new AppException(ErrorCode.RETURN_REQUEST_ALREADY_EXISTS);
        }
        var transactionLog = logRepository.findFirstByOrderIdAndActionTypeOrderByCreatedAtDesc(
                order.getId(), OrderActionType.PAYMENT_SUCCESS.getType());
        if (transactionLog == null) throw new AppException(ErrorCode.TRANCSACTION_LOG_NOT_FOUND);

        var orderReturn = OrderReturn.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .paymentMethod(order.getPaymentMethod().getId())
                .reason(request.getReason())
                .refundAmount(order.getTotalAmount())
                .status(OrderReturnEnums.PENDING.getDescription())
                .order(order)
                .user(user)
                .build();
        var response = mapper.toOrderReturnResponse(repository.save(orderReturn));
        logService.logReturn(
                orderReturn,
                OrderActionType.REFUND_REQUEST.getType(),
                transactionLog.getTransactionRef(),
                transactionLog.getTransactionNo(),
                order);
        return response;
    }

    @Transactional
    public OrderReturnResponse updateStatus(int id, OrderReturnStatusRequest request, HttpServletRequest httpRequest)
            throws JsonMappingException, JsonProcessingException {
        var orderReturn = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_RETURN_NOT_FOUND));
        var transactionLog = logRepository.findFirstByOrderIdAndActionTypeOrderByCreatedAtDesc(
                orderReturn.getOrder().getId(), OrderActionType.PAYMENT_SUCCESS.getType());
        if (!isValidStatus(request.getStatus())) {
            throw new AppException(ErrorCode.ORDER_RETURN_STATUS_NOT_FOUND);
        }
        // nếu hoàn tiền rồi thì không được sửa
        if (OrderReturnEnums.REFUNDED.getDescription().equalsIgnoreCase(orderReturn.getStatus())) {
            throw new AppException(ErrorCode.ORDER_STATUS_CANNOT_BE_MODIFIED);
        }

        mapper.toUpdateOrderStatus(orderReturn, request);
        if (request.getReason() != null && !request.getReason().isBlank()) {
            orderReturn.setReason(request.getReason());
        }
        orderReturn.setUpdatedAt(LocalDateTime.now());
        var response = mapper.toOrderReturnResponse(repository.save(orderReturn));
        if (request.getStatus().equalsIgnoreCase(OrderReturnEnums.REFUNDED.getDescription())) {
            //            vnpayService.refund(orderReturn, "admin", httpRequest);
            logService.logReturn(
                    orderReturn,
                    OrderActionType.UPDATE_STATUS.getType(),
                    transactionLog.getTransactionRef(),
                    transactionLog.getTransactionNo(),
                    orderReturn.getOrder());
        }
        return response;
    }

    public static boolean isValidStatus(String input) {
        return Arrays.stream(OrderReturnEnums.values())
                .anyMatch(status -> status.getDescription().equalsIgnoreCase(input));
    }
}
