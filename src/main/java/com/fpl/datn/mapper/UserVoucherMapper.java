package com.fpl.datn.mappers;

import org.springframework.stereotype.Component;

import com.fpl.datn.dtos.response.UserVoucherResponse;
import com.fpl.datn.models.ZUserVoucher;

@Component
public class UserVoucherMapper {

    public UserVoucherResponse toUserVoucherResponse(ZUserVoucher userVoucher) {
        if (userVoucher == null) {
            return null;
        }

        UserVoucherResponse response = new UserVoucherResponse();
        response.setId(userVoucher.getId());

        // Thông tin user
        if (userVoucher.getUser() != null) {
            response.setUserId(userVoucher.getUser().getId());
            response.setUserFullName(userVoucher.getUser().getFullName());
            response.setUserEmail(userVoucher.getUser().getEmail());
        }

        // Thông tin voucher
        if (userVoucher.getVoucher() != null) {
            response.setVoucherId(userVoucher.getVoucher().getId());
            response.setVoucherCode(userVoucher.getVoucher().getCode());
            response.setVoucherDescription(userVoucher.getVoucher().getDescription());
        }

        return response;
    }
}
