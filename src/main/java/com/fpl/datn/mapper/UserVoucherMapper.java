package com.fpl.datn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fpl.datn.dto.response.UserVoucherResponse;
import com.fpl.datn.models.ZUserVoucher;

@Mapper(componentModel = "spring")
public interface UserVoucherMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userFullName", source = "user.fullName")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "voucherId", source = "voucher.id")
    @Mapping(target = "voucherCode", source = "voucher.code")
    @Mapping(target = "voucherDescription", source = "voucher.description")
    UserVoucherResponse toVoucherResponse(ZUserVoucher userVoucher);
}
