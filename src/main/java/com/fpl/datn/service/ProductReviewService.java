package com.fpl.datn.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.fpl.datn.constant.PredefinedRole;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.response.ProductReviewResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.ProductReviewMapper;
import com.fpl.datn.repository.ProductReviewRepository;

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

    @PreAuthorize("hasAuthority('VIEW_PRODUCT')")
    public PageResponse<ProductReviewResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repository.findAll(pageable);
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
        var order = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_REVIEW_NOT_FOUND));
        return mapper.toProductReviewResponse(order);
    }

    public void delete(int id) {
        var user = userService.getMyInfo();
        var review = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_REVIEW_NOT_FOUND));

        if (!user.getId().equals(review.getUser().getId())
                && !user.hasRole(PredefinedRole.ROLE_ADMIN)
                && !user.hasRole(PredefinedRole.ROLE_MANAGER)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        repository.deleteById(id);
    }
}
