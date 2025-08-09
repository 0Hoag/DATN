package com.fpl.datn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.fpl.datn.dto.response.VoucherResponse;
import com.fpl.datn.dto.response.ZUserVoucherResponse;
import com.fpl.datn.models.ZUserVoucher;

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

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "code", source = "voucher.code")
    @Mapping(target = "description", source = "voucher.description")
    @Mapping(target = "discountValue", source = "voucher.discountValue")
    @Mapping(target = "minOrderValue", source = "voucher.minOrderValue")
    @Mapping(target = "startAt", source = "voucher.startAt")
    @Mapping(target = "endAt", source = "voucher.endAt")
    @Mapping(target = "isActive", source = "voucher.isActive")
    VoucherResponse UserVoucherResponse(ZUserVoucher zUserVoucher);
}
