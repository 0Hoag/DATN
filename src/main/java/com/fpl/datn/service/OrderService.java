package com.fpl.datn.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.OrderRequest;
import com.fpl.datn.dto.request.OrderStatusRequest;
import com.fpl.datn.dto.request.UpdateOrderRequest;
import com.fpl.datn.dto.response.OrderItemResponse;
import com.fpl.datn.dto.response.OrderResponse;
import com.fpl.datn.enums.OrderStatus;
import com.fpl.datn.enums.PaymentStatus;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.OrderMapper;
import com.fpl.datn.models.Order;
import com.fpl.datn.models.OrderDetail;
import com.fpl.datn.repository.AddressRepository;
import com.fpl.datn.repository.OrderDetailRepository;
import com.fpl.datn.repository.OrderRepository;
import com.fpl.datn.repository.PaymentMethodRepository;
import com.fpl.datn.repository.ProductVariantRepository;
import com.fpl.datn.repository.UserRepository;
import com.fpl.datn.specification.OrderSpecification;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository repository;
    OrderDetailRepository orderDetailRepository;
    UserRepository userRepository;
    AddressRepository addressRepository;
    PaymentMethodRepository paymentRepository;
    ProductVariantRepository variantRepository;
    OrderMapper mapper;

    public PageResponse<OrderResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repository.findAll(pageable);
        var data = pageData.stream()
                .map(order -> {
                    OrderResponse response = mapper.toOrderResponse(order);
                    if (order.getUser() != null && order.getPaymentMethod() != null) {
                        response.setFullName(order.getUser().getFullName());
                        response.setPhone(order.getUser().getPhone());
                        response.setPaymentMethod(order.getPaymentMethod().getName());
                    }
                    return response;
                })
                .toList();
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public OrderResponse getOrder(int id) {
        var order = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_EXISTED));
        OrderResponse response = mapper.toOrderResponse(order);
        if (order.getUser() != null && order.getPaymentMethod() != null) {
            response.setFullName(order.getUser().getFullName());
            response.setPhone(order.getUser().getPhone());
            response.setPaymentMethod(order.getPaymentMethod().getName());
        }
        return response;
    }

    public PageResponse<OrderResponse> Search(
            String keyword,
            Integer id,
            String orderStatus,
            String paymentStatus,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Specification<Order> spec = OrderSpecification.hasFullName(keyword)
                .and(OrderSpecification.hasId(id))
                .and(OrderSpecification.createAtBetween(startDate, endDate))
                .and(OrderSpecification.hasOrderStatus(orderStatus))
                .and(OrderSpecification.hasPaymentStatus(paymentStatus));
        var pageData = repository.findAll(spec, pageable);
        var data = pageData.stream()
                .map(order -> {
                    OrderResponse response = mapper.toOrderResponse(order);
                    if (order.getUser() != null && order.getPaymentMethod() != null) {
                        response.setFullName(order.getUser().getFullName());
                        response.setPhone(order.getUser().getPhone());
                        response.setPaymentMethod(order.getPaymentMethod().getName());
                    }
                    return response;
                })
                .toList();
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        var order = prepareOrder(request);
        var details = processOrderItems(order, request.getItems(), false);
        BigDecimal total = details.stream()
                .map(detail -> detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        order.setOrderDetails(details);
        repository.save(order);

        OrderResponse response = mapper.toOrderResponse(order);
        response.setFullName(order.getUser().getFullName());
        response.setPhone(order.getUser().getPhone());
        response.setPaymentMethod(order.getPaymentMethod().getName());

        return response;
    }

    // Chuẩn bị đặt hàng
    private Order prepareOrder(OrderRequest request) {
        var user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var address = addressRepository
                .findById(request.getAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        var payment = paymentRepository
                .findById(request.getPaymentMethodId())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND));

        var order = Order.builder()
                .user(user)
                .address(address)
                .paymentMethod(payment)
                .note(request.getNote())
                .orderStatus(OrderStatus.PENDING.getDescription())
                .paymentStatus(PaymentStatus.PENDING.getDescription())
                .createdAt(LocalDateTime.now())
                .isReturn(false)
                .build();
        return order;
    }

    // Xử lý đặt hàng
    private List<OrderDetail> processOrderItems(Order order, List<OrderItemResponse> items, boolean isUpdate) {
        List<OrderDetail> details = new ArrayList<>();

        // Dùng cho Update và Delete
        if (isUpdate && order.getOrderDetails() != null) {
            for (OrderDetail detail : order.getOrderDetails()) {
                var variant = detail.getProductVariant();
                variant.setQuantity(variant.getQuantity() + detail.getQuantity());
                variant.setSold(variant.getSold() - detail.getQuantity());
                variantRepository.save(variant);
            }
            orderDetailRepository.deleteAll(order.getOrderDetails());
            order.getOrderDetails().clear();
        }
        // Dùng cho Create
        if (!isUpdate && items != null) {
            for (OrderItemResponse response : items) {
                var variant = variantRepository
                        .findById(response.getProductVariantId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_VARIANT_NOT_FOUND));

                if (variant.getQuantity() < response.getQuantity()) {
                    throw new AppException(ErrorCode.PRODUCT_OUT_OF_STOCK);
                }

                variant.setQuantity(variant.getQuantity() - response.getQuantity());
                variant.setSold(variant.getSold() + response.getQuantity());
                variantRepository.save(variant);

                var detail = OrderDetail.builder()
                        .order(order)
                        .product(variant.getProduct())
                        .productVariant(variant)
                        .quantity(response.getQuantity())
                        .price(variant.getPrice())
                        .discount(BigDecimal.ZERO) // Chưa làm voucher
                        .build();

                details.add(detail);
            }
        }
        return details;
    }

    @Transactional // cái này mà sai thì nó rollback lại
    public OrderResponse update(int id, UpdateOrderRequest request) {
        var order = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        mapper.toUpdateOrder(order, request);
        var details = processOrderItems(order, request.getItems(), true);
        BigDecimal total = details.stream()
                .map(detail -> detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
        order.setOrderDetails(details);
        order.setUpdatedAt(LocalDateTime.now());

        repository.save(order);
        OrderResponse response = mapper.toOrderResponse(order);
        response.setFullName(order.getUser().getFullName());
        response.setPhone(order.getUser().getPhone());
        response.setPaymentMethod(order.getPaymentMethod().getName());
        return response;
    }

    @Transactional
    public void delete(int id) {
        var order = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        processOrderItems(order, Collections.emptyList(), true);

        repository.delete(order);
    }

    // Update trạng thái đơn hàng và Trạng thái thanh toán;
    @Transactional
    public OrderResponse updateOrderStatus(int id, OrderStatusRequest request) {
        var order = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (!isValidOrderStatus(request.getOrderStatus())) throw new AppException(ErrorCode.ORDER_STATUS_NOT_FOUND);

        if (!isValidPaymentStatus(request.getPaymentStatus()))
            throw new AppException(ErrorCode.PAYMENT_STATUS_NOT_FOUND);

        // Nếu đã giao hàng thì báo không thể chỉnh sửa
        if (request.getOrderStatus().equals(OrderStatus.DELIVERED.getDescription()))
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_MODIFIED);

        mapper.toUpdateStatus(order, request);
        if (request.getNote() != null && !request.getNote().isEmpty()) order.setNote(request.getNote());

        order.setUpdatedAt(LocalDateTime.now());
        repository.save(order);
        return mapper.toOrderResponse(order);
    }

    public static boolean isValidOrderStatus(String input) {
        return Arrays.stream(OrderStatus.values())
                .anyMatch(status -> status.getDescription().equalsIgnoreCase(input));
    }

    public static boolean isValidPaymentStatus(String input) {
        return Arrays.stream(PaymentStatus.values())
                .anyMatch(status -> status.getDescription().equalsIgnoreCase(input));
    }
}
