package com.fpl.datn.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserVoucherRequest {
    @NotNull(message = "userId không được để trống")
    Integer userId;

    @NotNull(message = "voucherId không được để trống")
    Integer voucherId;

    // Thêm cho chức năng bulk assign
    List<Integer> userIds;

    // Thêm cho chức năng claim voucher
    String voucherCode;
}