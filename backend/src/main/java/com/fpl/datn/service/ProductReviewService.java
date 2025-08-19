package com.fpl.datn.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.ProductReviewRequest;
import com.fpl.datn.dto.response.ProductReviewResponse;
import com.fpl.datn.enums.OrderStatus;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.ProductReviewMapper;
import com.fpl.datn.models.Product;
import com.fpl.datn.models.ProductReview;
import com.fpl.datn.models.User;
import com.fpl.datn.repository.OrderDetailRepository;
import com.fpl.datn.repository.ProductRepository;
import com.fpl.datn.repository.ProductReviewRepository;
import com.fpl.datn.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductReviewService {
    UserService userService;
    ProductReviewRepository repository;
    ProductReviewMapper mapper;
    ProductRepository productRepository;
    OrderDetailRepository orderDetailRepository;
    UserRepository userRepository;

    // ===== ADMIN: XEM TẤT CẢ ĐÁNH GIÁ =====
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public PageResponse<ProductReviewResponse> getAllReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repository.findAllByOrderByCreatedAtDesc(pageable);
        var data = pageData.stream().map(mapper::toProductReviewResponse).toList();

        return PageResponse.<ProductReviewResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    // ===== PUBLIC: XEM ĐÁNH GIÁ THEO SẢN PHẨM =====
    public PageResponse<ProductReviewResponse> getReviewsByProduct(Integer productId, int page, int size) {
        // Validate sản phẩm tồn tại
        productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repository.findByProductIdAndIsVisibleTrueOrderByCreatedAtDesc(productId, pageable);
        var data = pageData.stream().map(mapper::toProductReviewResponse).toList();

        return PageResponse.<ProductReviewResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    //     ===== FIX: CUSTOMER + ADMIN có thể tạo review =====
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ProductReviewResponse createReview(ProductReviewRequest request) {
        // Validate input
        if (request.getOrderDetailId() == null || request.getRating() == null) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new AppException(ErrorCode.RATING_INVALID);
        }

        // Lấy user hiện tại
        var currentUserResponse = userService.getMyInfo();
        User currentUser = userRepository
                .findById(currentUserResponse.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var orderDetail = orderDetailRepository
                .findById(request.getOrderDetailId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));

        if (orderDetail.getProductReview() != null) {
            throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        // Validate sản phẩm tồn tại và active
        Product product = orderDetail.getProduct();

        if (!product.getIsActive()) {
            throw new AppException(ErrorCode.PRODUCT_INACTIVE);
        }

        if (!orderDetail.getOrder().getOrderStatus().equals(OrderStatus.RECEIED.getDescription()))
            throw new AppException(ErrorCode.ORDER_NOT_RECEIVED);

        // Tạo đánh giá mới
        ProductReview review = ProductReview.builder()
                .product(product)
                .user(currentUser)
                .orderDetail(orderDetail)
                .rating(request.getRating())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        orderDetail.setIsReviewed(true);
        orderDetailRepository.save(orderDetail);
        var savedReview = repository.save(review);
        return mapper.toProductReviewResponse(savedReview);
    }

    // ===== ADMIN: XÓA ĐÁNH GIÁ =====
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public void deleteReview(int id) {
        var review = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_REVIEW_NOT_FOUND));
        repository.deleteById(review.getId());
    }

    public PageResponse<ProductReviewResponse> searchReviewsByProductName(String name, int page, int size) {
        validateProductName(name);
        var pageable = PageRequest.of(page - 1, size);
        var pageData = repository.searchByProductName(name.trim(), pageable);
        var data = pageData.stream().map(mapper::toProductReviewResponse).toList();
        return PageResponse.<ProductReviewResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    private void validateProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
    }
}