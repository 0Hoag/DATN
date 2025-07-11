package com.fpl.datn.service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.ClaimVoucherRequest;
import com.fpl.datn.dto.response.PublicVoucherResponse;
import com.fpl.datn.dto.response.VoucherClaimResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.models.User;
import com.fpl.datn.models.Voucher;
import com.fpl.datn.repository.UserRepository;
import com.fpl.datn.repository.UserVoucherRepository;
import com.fpl.datn.repository.VoucherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoucherGiftCodeService {

    VoucherRepository voucherRepository;
    UserRepository userRepository;
    UserVoucherRepository userVoucherRepository;
    VoucherUsageService voucherUsageService;

    // Claim voucher bằng code
    @Transactional
    public VoucherClaimResponse claimVoucher(ClaimVoucherRequest request) {
        // Validate user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Validate voucher
        Voucher voucher = voucherRepository.findByCode(request.getVoucherCode())
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        // Kiểm tra voucher có hợp lệ không
        validateVoucherForClaim(voucher);

        // Kiểm tra user đã claim voucher này chưa
        if (voucherUsageService.hasUserClaimedVoucher(request.getUserId(), voucher.getId())) {
            return VoucherClaimResponse.builder()
                    .success(false)
                    .message("Bạn đã sở hữu voucher này rồi!")
                    .voucherCode(voucher.getCode())
                    .build();
        }

        // Kiểm tra số lượng voucher còn lại
        int remainingQuantity = calculateRemainingQuantity(voucher);
        if (remainingQuantity <= 0) {
            return VoucherClaimResponse.builder()
                    .success(false)
                    .message("Voucher đã hết số lượng!")
                    .voucherCode(voucher.getCode())
                    .build();
        }

        // Claim voucher cho user
        voucherUsageService.recordVoucherClaim(user, voucher);

        // Cập nhật usage count
        voucher.setUsageCount(voucher.getUsageCount() + 1);
        voucherRepository.save(voucher);

        log.info("User {} successfully claimed voucher {}", request.getUserId(), request.getVoucherCode());

        return VoucherClaimResponse.builder()
                .success(true)
                .message("Claim voucher thành công!")
                .voucherCode(voucher.getCode())
                .description(voucher.getDescription())
                .discountValue(formatDiscount(voucher.getDiscountValue()))
                .remainingUses(calculateRemainingQuantity(voucher))
                .build();
    }

    // Lấy danh sách voucher công khai
    public PageResponse<PublicVoucherResponse> getPublicVouchers(Integer userId, int page, int size) {
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("endAt").ascending());

        var pageData = voucherRepository.findByIsActiveTrueAndStartAtBeforeAndEndAtAfter(now, now, pageable);

        var data = pageData.stream().map(voucher -> {
            boolean canClaim = userId != null && !voucherUsageService.hasUserClaimedVoucher(userId, voucher.getId());

            return PublicVoucherResponse.builder()
                    .code(voucher.getCode())
                    .description(voucher.getDescription())
                    .discountValue(voucher.getDiscountValue())
                    .minOrderValue(voucher.getMinOrderValue())
                    .remainingQuantity(calculateRemainingQuantity(voucher))
                    .endAt(voucher.getEndAt())
                    .isActive(voucher.getIsActive())
                    .canClaim(canClaim)
                    .build();
        }).collect(Collectors.toList());

        return PageResponse.<PublicVoucherResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    // Helper methods
    private void validateVoucherForClaim(Voucher voucher) {
        LocalDateTime now = LocalDateTime.now();

        if (!voucher.getIsActive()) {
            throw new AppException(ErrorCode.VOUCHER_INVALID);
        }

        if (voucher.getStartAt().isAfter(now)) {
            throw new AppException(ErrorCode.VOUCHER_INVALID);
        }

        if (voucher.getEndAt().isBefore(now)) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }

        int remainingQty = calculateRemainingQuantity(voucher);
        if (remainingQty <= 0) {
            throw new AppException(ErrorCode.VOUCHER_OVERUSED);
        }
    }

    private int calculateRemainingQuantity(Voucher voucher) {
        if (voucher.getQuantity() == null || voucher.getQuantity() <= 0) {
            return Integer.MAX_VALUE;
        }

        int usageCount = voucher.getUsageCount() != null ? voucher.getUsageCount() : 0;
        return Math.max(0, voucher.getQuantity() - usageCount);
    }

    private String formatDiscount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            return String.format("%.0fK", amount.divide(BigDecimal.valueOf(1000)));
        }
        return amount.toString() + "đ";
    }

    // Tạo voucher code có thể chia sẻ
    public String generateShareableVoucherCode(Integer voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        String shareMessage = String.format(
                "🎁 Mã giảm giá %s - Giảm %s!\n" +
                        "Sử dụng mã: %s\n" +
                        "Áp dụng cho đơn hàng từ %s\n" +
                        "Có hiệu lực đến: %s\n" +
                        "Nhanh tay claim ngay! 🔥",
                voucher.getDescription(),
                formatDiscount(voucher.getDiscountValue()),
                voucher.getCode(),
                formatDiscount(voucher.getMinOrderValue()),
                voucher.getEndAt().toLocalDate()
        );

        return shareMessage;
    }
}