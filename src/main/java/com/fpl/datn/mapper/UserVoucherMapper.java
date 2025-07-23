package com.fpl.datn.mapper;

import com.fpl.datn.dto.response.ZUserVoucherResponse;
import com.fpl.datn.models.ZUserVoucher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserVoucherMapper {
    UserVoucherMapper INSTANCE = Mappers.getMapper(UserVoucherMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "userFullName")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "voucher.id", target = "voucherId")
    @Mapping(source = "voucher.code", target = "voucherCode")
    @Mapping(source = "voucher.discountValue", target = "voucherDiscountValue")
    @Mapping(source = "voucher.minOrderValue", target = "voucherMinOrderValue")
    @Mapping(source = "voucher.startAt", target = "voucherStartAt")
    @Mapping(source = "voucher.endAt", target = "voucherEndAt")
    @Mapping(source = "voucher.quantity", target = "voucherQuantity")
    @Mapping(source = "voucher.type", target = "voucherType") // Ánh xạ trường 'type' từ voucher
    @Mapping(source = "voucher.isActive", target = "voucherIsActive")
    ZUserVoucherResponse toZUserVoucherResponse(ZUserVoucher zUserVoucher);
}
