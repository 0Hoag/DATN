package com.fpl.datn.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.fpl.datn.dto.request.UpdateVoucherRequest;
import com.fpl.datn.dto.request.VoucherRequest;
import com.fpl.datn.dto.response.VoucherResponse;
import com.fpl.datn.models.Voucher;

@Mapper(componentModel = "Spring")
public interface VoucherMapper {
    VoucherResponse toVoucherResponse(Voucher voucher);

    // cái nào không map được thì nó bỏ qua(tắt cảnh báo thôi)
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    Voucher toVoucherRequest(VoucherRequest request);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void toUpdateVoucher(@MappingTarget Voucher voucher, UpdateVoucherRequest request);
}
