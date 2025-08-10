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
import com.fpl.datn.dto.response.ProductReviewStatsResponse;
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

        if (!product.getIsActive()) throw new AppException(ErrorCode.PRODUCT_INACTIVE);

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

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void hideReview(Integer orderDetailId) {
        var orderDetail = orderDetailRepository
                .findById(orderDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAIL_NOT_FOUND));
        var review = repository
                .findById(orderDetail.getProductReview().getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_REVIEW_NOT_FOUND));

        review.setIsVisible(false);
        repository.save(review);
    }

    // ===== FIX: CUSTOMER + ADMIN có thể xóa review của mình =====
    @Transactional
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public void deleteMyReview(int id) {
        var currentUserResponse = userService.getMyInfo();
        User currentUser = userRepository
                .findById(currentUserResponse.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var review = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_REVIEW_NOT_FOUND));

        // Kiểm tra quyền sở hữu (trừ ADMIN có thể xóa bất kỳ)
        boolean isAdmin =
                currentUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));

        if (!isAdmin && !review.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        repository.deleteById(id);
    }

    // ===== MỚI: ĐẾM SỐ ĐÁNH GIÁ THEO SẢN PHẨM =====
    public ProductReviewStatsResponse getReviewStats(Integer productId) {
        // Validate sản phẩm tồn tại
        Product product =
                productRepository.findById(productId).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Đếm tổng số đánh giá
        Long totalReviews = repository.countByProductId(productId);

        // Tính rating trung bình
        Double averageRating = repository.getAverageRatingByProductId(productId);
        if (averageRating == null) {
            averageRating = 0.0;
        }

        // Đếm số đánh giá theo từng sao
        Long fiveStars = repository.countByProductIdAndRating(productId, 5);
        Long fourStars = repository.countByProductIdAndRating(productId, 4);
        Long threeStars = repository.countByProductIdAndRating(productId, 3);
        Long twoStars = repository.countByProductIdAndRating(productId, 2);
        Long oneStar = repository.countByProductIdAndRating(productId, 1);

        return ProductReviewStatsResponse.builder()
                .productId(productId)
                .productName(product.getName())
                .totalReviews(totalReviews)
                .averageRating(Math.round(averageRating * 10.0) / 10.0) // Làm tròn 1 chữ số thập phân
                .fiveStars(fiveStars)
                .fourStars(fourStars)
                .threeStars(threeStars)
                .twoStars(twoStars)
                .oneStar(oneStar)
                .build();
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

    // ===== FIX: CUSTOMER + ADMIN có thể check review status =====
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public boolean hasUserReviewedProduct(Integer productId) {
        var currentUserResponse = userService.getMyInfo();
        return repository.existsByUserIdAndProductId(currentUserResponse.getId(), productId);
    }

    // ===== FIX: CUSTOMER + ADMIN có thể check purchase status =====
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public boolean hasUserPurchasedProduct(Integer productId) {
        var currentUserResponse = userService.getMyInfo();
        return orderDetailRepository.hasUserPurchasedProduct(currentUserResponse.getId(), productId);
    }

    private void validateProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
    }
}
