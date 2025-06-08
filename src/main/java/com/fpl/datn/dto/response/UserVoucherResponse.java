package com.fpl.datn.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVoucherResponse {
    private Integer id;
    private Integer userId;
    private String userFullName;
    private String userEmail;
    private Integer voucherId;
    private String voucherCode;
    private String voucherDescription;
}
