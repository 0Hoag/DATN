package com.fpl.datn.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.OrderRequest;
import com.fpl.datn.dto.request.OrderStatusRequest;
import com.fpl.datn.dto.request.UpdateOrderRequest;
import com.fpl.datn.dto.response.OrderItemResponse;
import com.fpl.datn.dto.response.OrderResponse;
import com.fpl.datn.enums.OrderActionType;
import com.fpl.datn.enums.OrderStatus;
import com.fpl.datn.enums.PaymentMethod;
import com.fpl.datn.enums.PaymentStatus;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.OrderMapper;
import com.fpl.datn.models.Address;
import com.fpl.datn.models.Order;
import com.fpl.datn.models.OrderDetail;
import com.fpl.datn.models.Voucher;
import com.fpl.datn.repository.AddressRepository;
import com.fpl.datn.repository.OrderDetailRepository;
import com.fpl.datn.repository.OrderRepository;
import com.fpl.datn.repository.PaymentMethodRepository;
import com.fpl.datn.repository.ProductVariantRepository;
import com.fpl.datn.repository.UserRepository;
import com.fpl.datn.repository.VoucherRepository;
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
    VoucherRepository voucherRepository;
    OrderMapper mapper;
    TransactionLogService logService;
    VnpayService vnpayService;

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('VIEW_ORDER')")
    public PageResponse<OrderResponse> getAll(int page, int size, boolean isDesc) {
        Sort sort = isDesc ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = repository.findByIsDeleteFalse(pageable);
        var data = pageData.stream().map(order -> mapper.toOrderResponse(order)).toList();
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public OrderResponse getOrder(int id) {
        var order = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (repository.existsByIdAndIsDeleteTrue(id)) throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        return mapper.toOrderResponse(order);
    }

    public PageResponse<OrderResponse> search(
            String keyword,
            Integer id,
            String phone,
            String orderStatus,
            String paymentStatus,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size,
            boolean isDesc) {
        Sort sort = isDesc ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Specification<Order> spec = OrderSpecification.hasFullName(keyword)
                .and(OrderSpecification.hasId(id))
                .and(OrderSpecification.hasPhone(phone))
                .and(OrderSpecification.hasOrderStatus(orderStatus))
                .and(OrderSpecification.hasPaymentStatus(paymentStatus))
                .and(OrderSpecification.createAtBetween(startDate, endDate));
        var pageData = repository.findAll(spec, pageable);
        var data = pageData.stream().map(order -> mapper.toOrderResponse(order)).toList();
        return PageResponse.<OrderResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    @Transactional
    public OrderResponse create(OrderRequest request, HttpServletRequest httpRequest) {
        var order = prepareOrder(request);
        var details = processOrderItems(order, request.getItems(), false);

        // tính tổng tiền
        BigDecimal total = calculateTotal(details);

        // Áp dụng voucher nếu có
        total = applyVoucher(order, total);

        order.setTotalAmount(total);
        order.setOrderDetails(details);
        repository.save(order);
        var response = mapper.toOrderResponse(order);
        String txnRef = null;
        if (isVnpay(order)) {
            var payment = vnpayService.createPaymentUrl(order, httpRequest);
            response.setPaymentUrl(payment.getPaymentUrl());
            txnRef = payment.getTxnRef();
        }
        logService.logPayment(order, OrderActionType.CREATE.getType(), txnRef, null);
        return response;
    }

    @Transactional
    public OrderResponse update(int id, UpdateOrderRequest request, HttpServletRequest httpRequest) {
        var order = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (repository.existsByIdAndIsDeleteTrue(id)) throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        mapper.toUpdateOrder(order, request);

        var user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var address = addressRepository
                .findById(request.getAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        var paymentMethod = paymentRepository
                .findById(request.getPaymentMethodId())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND));

        order.setUser(user);
        order.setAddress(address);
        order.setPaymentMethod(paymentMethod);
        order.setUpdatedAt(LocalDateTime.now());

        repository.save(order);
        var response = mapper.toOrderResponse(order);
        if (isVnpay(order)) {
            var paymentUrl = vnpayService.createPaymentUrl(order, httpRequest);
            response.setPaymentUrl(paymentUrl.getPaymentUrl());
        }
        return response;
    }

    @Transactional
    public void delete(int id) {
        var order = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (repository.existsByIdAndIsDeleteTrue(id)) throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        restoreInventory(order);
        order.setOrderStatus(OrderStatus.CANCELLED.getDescription());
        order.setIsDelete(true);
        repository.save(order);
        logService.logPayment(order, OrderActionType.DELETE.getType(), null, null);
    }

    // Update trạng thái đơn hàng và Trạng thái thanh toán;
    @Transactional
    public OrderResponse updateOrderStatus(int id, OrderStatusRequest request) {
        var order = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        if (repository.existsByIdAndIsDeleteTrue(id)) throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        // Nếu đã nhận hàng thì báo không thể chỉnh sửa
        if (order.getOrderStatus().equalsIgnoreCase(OrderStatus.RECEIED.getDescription()))
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_MODIFIED);

        if (request.getOrderStatus() != null) {
            if (!isValidOrderStatus(request.getOrderStatus())) throw new AppException(ErrorCode.ORDER_STATUS_NOT_FOUND);
            order.setOrderStatus(request.getOrderStatus());
        }

        if (request.getPaymentStatus() != null) {
            if (!isValidPaymentStatus(request.getPaymentStatus()))
                throw new AppException(ErrorCode.PAYMENT_STATUS_NOT_FOUND);
            order.setPaymentStatus(request.getPaymentStatus());
        }

        mapper.toUpdateStatus(order, request);
        if (request.getNote() != null && !request.getNote().isEmpty()) order.setNote(request.getNote());

        order.setUpdatedAt(LocalDateTime.now());
        repository.save(order);
        var response = mapper.toOrderResponse(order);
        if (isValidPaymentCOD(order) || isValidPaymentVNPAY(order)) {
            logService.logPayment(order, OrderActionType.UPDATESTATUS.getType(), response.getPaymentUrl(), null);
        }
        return response;
    }

    public static boolean isValidPaymentCOD(Order order) {
        return order.getPaymentMethod().getName().equalsIgnoreCase(PaymentMethod.COD.name())
                && order.getOrderStatus().equalsIgnoreCase(OrderStatus.RECEIED.getDescription());
    }

    public static boolean isValidPaymentVNPAY(Order order) {
        return order.getPaymentMethod().getName().equalsIgnoreCase(PaymentMethod.VNPAY.name())
                && order.getPaymentStatus().equalsIgnoreCase(PaymentStatus.PAID.getDescription());
    }

    public static boolean isValidOrderStatus(String input) {
        return Arrays.stream(OrderStatus.values())
                .anyMatch(status -> status.getDescription().equalsIgnoreCase(input));
    }

    public static boolean isValidPaymentStatus(String input) {
        return Arrays.stream(PaymentStatus.values())
                .anyMatch(status -> status.getDescription().equalsIgnoreCase(input));
    }

    // Chuẩn bị đặt hàng
    @PreAuthorize("hasAuthority('BUY_PRODUCT')")
    private Order prepareOrder(OrderRequest request) {
        var user = userRepository
                .findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var payment = paymentRepository
                .findById(request.getPaymentMethodId())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_METHOD_NOT_FOUND));
        Voucher voucher = null;
        if (request.getVoucherId() != null) {
            voucher = voucherRepository
                    .findById(request.getVoucherId())
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        }
        Address address = null;
        if (request.getAddressId() == null && request.getInputAddress() != null) {
            address = Address.builder()
                    .addressLine(request.getInputAddress())
                    .fullName(request.getInputFullname())
                    .phone(request.getInputPhone())
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            addressRepository.save(address);
        } else {
            address = addressRepository
                    .findById(request.getAddressId())
                    .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        }
        String orderStatus = OrderStatus.PENDING.getDescription();
        String paymentStaus = PaymentStatus.PENDING.getDescription();
        if (request.getOrderStatus() != null) {
            if (!isValidOrderStatus(request.getOrderStatus())) throw new AppException(ErrorCode.ORDER_STATUS_NOT_FOUND);
            orderStatus = request.getOrderStatus();
        }

        if (request.getPaymentStatus() != null) {
            if (!isValidPaymentStatus(request.getPaymentStatus()))
                throw new AppException(ErrorCode.PAYMENT_STATUS_NOT_FOUND);
            paymentStaus = request.getPaymentStatus();
        }

        var order = Order.builder()
                .user(user)
                .address(address)
                .paymentMethod(payment)
                .note(request.getNote())
                .orderStatus(orderStatus)
                .paymentStatus(paymentStaus)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isReturn(false)
                .isDelete(false)
                .voucher(voucher)
                .build();
        return order;
    }

    // Xử lý đặt hàng
    private List<OrderDetail> processOrderItems(Order order, List<OrderItemResponse> items, boolean isDelete) {
        if (isDelete) restoreInventory(order);
        return createOrderDetails(order, items);
    }

    // Xử lí hoàn hàng tồn kho
    private void restoreInventory(Order order) {
        if (order.getOrderDetails() != null) {
            for (OrderDetail detail : order.getOrderDetails()) {
                var variant = detail.getProductVariant();
                variant.setQuantity(variant.getQuantity() + detail.getQuantity());
                variant.setSold(variant.getSold() - detail.getQuantity());
                variantRepository.save(variant);
            }
            orderDetailRepository.deleteAll(order.getOrderDetails());
            order.getOrderDetails().clear();
        }
    }
    // Xử lí tạo hàng tồn kho
    private List<OrderDetail> createOrderDetails(Order order, List<OrderItemResponse> items) {
        List<OrderDetail> details = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
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
                        .build();
                details.add(detail);
            }
        }
        return details;
    }

    // Xử lí tính tổng tiền
    private BigDecimal calculateTotal(List<OrderDetail> details) {
        return details.stream()
                .map(detail -> detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Xử lí thanh toán
    private boolean isVnpay(Order order) {
        return PaymentMethod.VNPAY
                .name()
                .equalsIgnoreCase(order.getPaymentMethod().getName());
    }

    // Xử lí voucher
    private BigDecimal applyVoucher(Order order, BigDecimal total) {
        var voucher = order.getVoucher();
        if (voucher == null) return total;
        boolean isExpired = voucher.getStartAt().isAfter(LocalDateTime.now())
                || voucher.getEndAt().isBefore(LocalDateTime.now());
        boolean isOverused = voucher.getUsageCount() >= voucher.getQuantity();
        boolean notEnoughMinOrder =
                voucher.getMinOrderValue() != null && total.compareTo(voucher.getMinOrderValue()) < 0;
        if (!Boolean.TRUE.equals(voucher.getIsActive()) || isExpired || isOverused) {
            throw new AppException(ErrorCode.VOUCHER_INVALID);
        }
        if (isExpired) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }
        if (isOverused) {
            throw new AppException(ErrorCode.VOUCHER_OVERUSED);
        }
        if (notEnoughMinOrder) {
            throw new AppException(ErrorCode.VOUCHER_MIN_ORDER_NOT_MET);
        }

        BigDecimal discount = (voucher.getDiscountValue().compareTo(BigDecimal.valueOf(100)) <= 0)
                ? total.multiply(voucher.getDiscountValue()).divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
                : voucher.getDiscountValue();
        total = total.subtract(discount.min(total)); // tránh âm

        // Cập nhật voucher
        voucher.setUsageCount(voucher.getUsageCount() + 1);
        voucherRepository.save(voucher);
        return total;
    }
}
