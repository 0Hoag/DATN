package com.fpl.datn.mapper;

import org.mapstruct.Mapper;

import com.fpl.datn.dto.response.UserVoucherResponse;
import com.fpl.datn.models.ZUserVoucher;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class})
public interface UserVoucherMapper {
    UserVoucherResponse toVoucherResponse(ZUserVoucher userVoucher);
}
