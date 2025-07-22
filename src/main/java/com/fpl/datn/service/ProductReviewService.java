package com.fpl.datn.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpl.datn.constant.PredefinedRole;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.ProductReviewRequest;
import com.fpl.datn.dto.response.ProductReviewResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.ProductReviewMapper;
import com.fpl.datn.models.Product;
import com.fpl.datn.models.ProductReview;
import com.fpl.datn.models.Role;
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
    UserRepository userRepository; // ĐÃ THÊM DEPENDENCY

    @PreAuthorize("hasAuthority('VIEW_PRODUCT') or hasRole('ADMIN')")
    public PageResponse<ProductReviewResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repository.findByParentReviewIsNull(pageable);
        var data = pageData.stream().map(mapper::toProductReviewResponse).toList();

        return PageResponse.<ProductReviewResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    @PreAuthorize("hasAuthority('VIEW_PRODUCT')")
    public ProductReviewResponse getReview(int id) {
        var review = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_REVIEW_NOT_FOUND));
        return mapper.toProductReviewResponse(review);
    }

    @Transactional
    @PreAuthorize("hasAuthority('CREATE_PRODUCT_REVIEW') or hasAuthority('REPLY_PRODUCT_REVIEW')")
    public ProductReviewResponse createReview(ProductReviewRequest request) {
        // Lấy UserResponse từ UserService
        var currentUserResponse = userService.getMyInfo();
        // Tìm nạp User entity đầy đủ từ UserRepository
        User currentUser = userRepository
                .findById(currentUserResponse.getId()) // Giả định UserResponse có getId()
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)); // Sử dụng mã lỗi của bạn

        ProductReview review = new ProductReview();
        Product product;

        // Kiểm tra nếu đây là bình luận trả lời (admin reply)
        if (request.getReplyTo() != null) {
            // 1. Lấy bình luận gốc
            ProductReview parentReview = repository
                    .findById(request.getReplyTo())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_REVIEW_NOT_FOUND));

            // 2. Kiểm tra xem bình luận gốc có phải là bình luận cấp cao nhất không (không phải là reply của reply)
            if (parentReview.getParentReview() != null) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }

            // 3. Kiểm tra xem bình luận gốc đã có admin reply chưa
            if (parentReview.getAdminReply() != null) {
                throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTS);
            }

            // 4. Kiểm tra quyền của user (chỉ admin/manager mới được reply)
            Set<Role> userRoles = currentUser.getRoles();
            boolean isAdminOrManager = userRoles != null
                    && (userRoles.stream().anyMatch(role -> role.getName().equals(PredefinedRole.ROLE_ADMIN))
                            || userRoles.stream()
                                    .anyMatch(role -> role.getName().equals(PredefinedRole.ROLE_MANAGER)));
            if (!isAdminOrManager) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }

            // 5. Thiết lập thông tin cho bình luận trả lời
            review.setParentReview(parentReview);
            review.setProduct(parentReview.getProduct());
            review.setUser(currentUser);
            review.setContent(request.getContent());
            review.setRating(null); // Bình luận trả lời của admin không có rating

            product = parentReview.getProduct();

        } else { // Đây là bình luận gốc của người dùng
            // 1. Validate productId và rating phải có cho bình luận gốc
            if (request.getProductId() == null || request.getRating() == null) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }

            // 2. Validate sản phẩm tồn tại và active
            product = productRepository
                    .findById(request.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            if (!product.getIsActive()) {
                throw new AppException(ErrorCode.PRODUCT_INACTIVE);
            }

            // 3. Kiểm tra user đã mua sản phẩm này chưa
            if (!orderDetailRepository.hasUserPurchasedProduct(currentUser.getId(), request.getProductId())) {
                throw new AppException(ErrorCode.USER_NOT_PURCHASED_PRODUCT);
            }

            // 4. Kiểm tra user đã đánh giá sản phẩm này chưa (chỉ cho bình luận gốc)
            if (repository.existsByUserIdAndProductId(currentUser.getId(), request.getProductId())) {
                throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTS);
            }

            // 5. Thiết lập thông tin cho bình luận gốc
            review.setProduct(product);
            review.setUser(currentUser);
            review.setRating(request.getRating());
            review.setContent(request.getContent());
            review.setParentReview(null);
        }

        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        // Lưu vào database
        var savedReview = repository.save(review);

        // Nếu là bình luận trả lời, cập nhật mối quan hệ adminReply trong parentReview
        if (request.getReplyTo() != null) {
            ProductReview parentReview = savedReview.getParentReview();
            parentReview.setAdminReply(savedReview);
            repository.save(parentReview); // Lưu lại parentReview để cập nhật mối quan hệ
        }

        return mapper.toProductReviewResponse(savedReview);
    }

    @Transactional
    public void delete(int id) {
        // Lấy UserResponse từ UserService
        var currentUserResponse = userService.getMyInfo();
        // Tìm nạp User entity đầy đủ từ UserRepository
        User user = userRepository
                .findById(currentUserResponse.getId()) // Giả định UserResponse có getId()
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)); // Sử dụng mã lỗi của bạn

        var review = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_REVIEW_NOT_FOUND));

        // Nếu là bình luận gốc, kiểm tra xem có reply không
        if (review.getParentReview() == null && review.getAdminReply() != null) {
            throw new AppException(ErrorCode.CANNOT_DELETE_REVIEW_WITH_REPLY);
        }

        // Nếu là bình luận trả lời, kiểm tra quyền xóa
        Set<Role> userRoles = user.getRoles();
        boolean isAdminOrManager = userRoles != null
                && (userRoles.stream().anyMatch(role -> role.getName().equals(PredefinedRole.ROLE_ADMIN))
                        || userRoles.stream().anyMatch(role -> role.getName().equals(PredefinedRole.ROLE_MANAGER)));
        if (review.getParentReview() != null && !isAdminOrManager) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Kiểm tra quyền xóa: chỉ chủ sở hữu hoặc admin/manager
        if (!user.getId().equals(review.getUser().getId()) && !isAdminOrManager) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        repository.deleteById(id);
    }
}
