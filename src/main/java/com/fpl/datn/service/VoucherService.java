package com.fpl.datn.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.UpdateVoucherRequest;
import com.fpl.datn.dto.request.VoucherRequest;
import com.fpl.datn.dto.response.VoucherResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.VoucherMapper;
import com.fpl.datn.repository.VoucherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherService {
    VoucherRepository repository;
    VoucherMapper mapper;

    public PageResponse<VoucherResponse> getAll(int page, int size, boolean isDesc) {
        Sort sort = isDesc ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = repository.findAll(pageable);
        var data = pageData.stream().map(mapper::toVoucherResponse).toList();
        return PageResponse.<VoucherResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public VoucherResponse getVoucher(int id) {
        var voucher = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        return mapper.toVoucherResponse(voucher);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public VoucherResponse create(VoucherRequest request) {
        var voucher = mapper.toVoucherRequest(request);
        voucher.setCreatedAt(LocalDateTime.now());

        repository.save(voucher);

        return mapper.toVoucherResponse(voucher);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public VoucherResponse update(int id, UpdateVoucherRequest request) {
        var voucher = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        mapper.toUpdateVoucher(voucher, request);
        voucher.setUpdatedAt(LocalDateTime.now());

        repository.save(voucher);

        return mapper.toVoucherResponse(voucher);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public void delete(int id) {
        if (!repository.existsById(id)) throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        repository.deleteById(id);
    }
}
