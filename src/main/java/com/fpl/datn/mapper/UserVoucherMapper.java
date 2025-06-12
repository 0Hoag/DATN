package com.fpl.datn.mapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.fpl.datn.dto.response.UserVoucherResponse;
import com.fpl.datn.models.ZUserVoucher;

@Component
public class UserVoucherMapper {

    public UserVoucherResponse toUserVoucherResponse(ZUserVoucher userVoucher) {
        if (userVoucher == null) {
            return null;
        }

        UserVoucherResponse response = new UserVoucherResponse();
        response.setId(userVoucher.getId());

        // Thông tin user đầy đủ
        if (userVoucher.getUser() != null) {
            response.setUserId(userVoucher.getUser().getId());
            response.setUserFullName(userVoucher.getUser().getFullName());
            response.setUserEmail(userVoucher.getUser().getEmail());
            response.setUserPhone(userVoucher.getUser().getPhone());
            response.setUserIsEnable(userVoucher.getUser().getIsEnable());
        }

        // Thông tin voucher đầy đủ
        if (userVoucher.getVoucher() != null) {
            response.setVoucherId(userVoucher.getVoucher().getId());
            response.setVoucherCode(userVoucher.getVoucher().getCode());
            response.setVoucherDescription(userVoucher.getVoucher().getDescription());
            response.setDiscountValue(userVoucher.getVoucher().getDiscountValue());
            response.setMinOrderValue(userVoucher.getVoucher().getMinOrderValue());
            response.setQuantity(userVoucher.getVoucher().getQuantity());
            response.setUsageCount(userVoucher.getVoucher().getUsageCount());
            response.setStartAt(userVoucher.getVoucher().getStartAt());
            response.setEndAt(userVoucher.getVoucher().getEndAt());
            response.setIsActive(userVoucher.getVoucher().getIsActive());

            // Xác định trạng thái voucher
            LocalDateTime now = LocalDateTime.now();
            if (!userVoucher.getVoucher().getIsActive()) {
                response.setVoucherStatus("INACTIVE");
            } else if (userVoucher.getVoucher().getEndAt().isBefore(now)) {
                response.setVoucherStatus("EXPIRED");
            } else {
                response.setVoucherStatus("ACTIVE");
            }

            // Xác định loại voucher dựa vào giá trị discountValue
            if (userVoucher.getVoucher().getDiscountValue().compareTo(BigDecimal.valueOf(1)) <= 0) {
                response.setVoucherType("PERCENT"); // Nếu discountValue <= 1 thì là giảm %
            } else {
                response.setVoucherType("FIXED"); // Còn lại là giảm tiền mặt
            }

            // Tính số lượng còn lại
            if (userVoucher.getVoucher().getQuantity() == -1) {
                response.setRemainingUses(-1); // Không giới hạn
            } else {
                response.setRemainingUses(userVoucher.getVoucher().getQuantity() - userVoucher.getVoucher().getUsageCount());
            }
        }

        return response;
    }
}
