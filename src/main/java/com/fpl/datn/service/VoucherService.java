package com.fpl.datn.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.UpdateVoucherRequest;
import com.fpl.datn.dto.request.VoucherRequest;
import com.fpl.datn.dto.response.VoucherResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.VoucherMapper;
import com.fpl.datn.models.Voucher;
import com.fpl.datn.repository.UserVoucherRepository;
import com.fpl.datn.repository.VoucherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherService {
    VoucherRepository repository;
    UserVoucherRepository userVoucherRepository;
    VoucherMapper mapper;

    @Transactional
    public PageResponse<VoucherResponse> getAll(int page, int size, boolean isDesc) {
        // Tự động cập nhật voucher hết hạn
        checkAndUpdateExpiredVouchers();

        Sort sort = isDesc ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = repository.findAll(pageable);
        var data = pageData.stream().map(mapper::toVoucherResponse).toList();
        return PageResponse.<VoucherResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    @Transactional
    public VoucherResponse getVoucher(int id) {
        var voucher = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        // Kiểm tra và cập nhật nếu voucher hết hạn
        if (voucher.getIsActive() && voucher.getEndAt().isBefore(LocalDateTime.now())) {
            voucher.setIsActive(false);
            voucher.setUpdatedAt(LocalDateTime.now());
            repository.save(voucher);
        }

        return mapper.toVoucherResponse(voucher);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public VoucherResponse create(VoucherRequest request) {
        // Chỉ check trùng code thôi
        if (repository.existsByCode(request.getCode())) {
            throw new AppException(ErrorCode.CATEGORY_NAME_EXISTED);
        }

        // Validate cơ bản
        validateBasic(request.getCode(), request.getDiscountValue(), request.getStartAt(), request.getEndAt());

        var voucher = mapper.toVoucherRequest(request);
        voucher.setCreatedAt(LocalDateTime.now());

        // Kiểm tra nếu voucher được tạo đã hết hạn thì set isActive = false
        if (isExpired(voucher)) {
            voucher.setIsActive(false);
        }

        repository.save(voucher);

        return mapper.toVoucherResponse(voucher);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Transactional
    public VoucherResponse update(int id, UpdateVoucherRequest request) {
        var voucher = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        // Check trùng code nếu có thay đổi
        if (request.getCode() != null && !request.getCode().equals(voucher.getCode())) {
            if (repository.existsByCode(request.getCode())) {
                throw new AppException(ErrorCode.CATEGORY_NAME_EXISTED);
            }
        }

        // Validate cơ bản cho update
        if (request.getCode() != null && request.getCode().trim().isEmpty()) {
            throw new AppException(ErrorCode.MISSING_INPUT);
        }
        if (request.getDiscountValue() != null
                && request.getDiscountValue().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.MISSING_INPUT);
        }

        mapper.toUpdateVoucher(voucher, request);
        voucher.setUpdatedAt(LocalDateTime.now());

        // Kiểm tra nếu voucher sau khi update bị hết hạn thì set isActive = false
        if (isExpired(voucher)) {
            voucher.setIsActive(false);
        }

        repository.save(voucher);

        return mapper.toVoucherResponse(voucher);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void delete(int id) {
        if (!repository.existsById(id)) {
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }

        // Check voucher có đang được sử dụng không
        if (userVoucherRepository.existsByVoucherId(id)) {
            throw new AppException(ErrorCode.CART_ITEM_ALREADY_EXISTS);
        }

        repository.deleteById(id);
    }

    // Hàm search voucher
    @Transactional
    public PageResponse<VoucherResponse> search(String keyword, int page, int size, boolean isDesc) {
        // Cập nhật trạng thái voucher hết hạn trước khi search
        updateExpiredVouchers();

        Sort sort = isDesc ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData =
                repository.findByCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable);

        var data = pageData.stream().map(mapper::toVoucherResponse).toList();

        return PageResponse.<VoucherResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    // Method để cập nhật tất cả voucher hết hạn
    @Transactional
    public void updateExpiredVouchers() {
        LocalDateTime now = LocalDateTime.now();
        var expiredVouchers = repository.findByIsActiveTrueAndEndAtBefore(now);

        if (!expiredVouchers.isEmpty()) {
            expiredVouchers.forEach(voucher -> {
                voucher.setIsActive(false);
                voucher.setUpdatedAt(now);
            });
            repository.saveAll(expiredVouchers);
        }
    }

    // Method để lấy số lượng voucher hết hạn
    public long countExpiredVouchers() {
        LocalDateTime now = LocalDateTime.now();
        return repository.countByIsActiveTrueAndEndAtBefore(now);
    }

    // Method để lấy danh sách voucher sắp hết hạn (trong vòng X ngày)
    public PageResponse<VoucherResponse> getExpiringVouchers(int days, int page, int size) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(days);

        Sort sort = Sort.by(Sort.Direction.ASC, "endAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = repository.findByIsActiveTrueAndEndAtBetween(now, futureDate, pageable);
        var data = pageData.stream().map(mapper::toVoucherResponse).toList();

        return PageResponse.<VoucherResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    private void validateBasic(
            String code, java.math.BigDecimal discountValue, LocalDateTime startAt, LocalDateTime endAt) {
        if (code == null || code.trim().isEmpty()) {
            throw new AppException(ErrorCode.MISSING_INPUT);
        }
        if (discountValue == null || discountValue.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.MISSING_INPUT);
        }
        if (startAt == null || endAt == null) {
            throw new AppException(ErrorCode.MISSING_INPUT);
        }
        if (startAt.isAfter(endAt)) {
            throw new AppException(ErrorCode.INVALID_DOB);
        }
    }

    private boolean isExpired(Voucher voucher) {
        LocalDateTime now = LocalDateTime.now();
        return voucher.getEndAt().isBefore(now);
    }

    private void checkAndUpdateExpiredVouchers() {
        LocalDateTime now = LocalDateTime.now();
        var expiredVouchers = repository.findByIsActiveTrueAndEndAtBefore(now);

        if (!expiredVouchers.isEmpty()) {
            expiredVouchers.forEach(voucher -> {
                voucher.setIsActive(false);
                voucher.setUpdatedAt(now);
            });
            repository.saveAll(expiredVouchers);
        }
    }
}
