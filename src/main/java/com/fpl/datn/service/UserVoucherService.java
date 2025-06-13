package com.fpl.datn.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.response.UserVoucherResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.UserVoucherMapper;
import com.fpl.datn.models.ZUserVoucher;
import com.fpl.datn.repository.UserRepository;
import com.fpl.datn.repository.UserVoucherRepository;
import com.fpl.datn.repository.VoucherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserVoucherService {
    UserVoucherRepository repository;
    UserRepository userRepository;
    VoucherRepository voucherRepository;
    UserVoucherMapper mapper;

    public PageResponse<UserVoucherResponse> getAll(int page, int size, boolean isDesc) {
        Sort sort = isDesc ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = repository.findAll(pageable);
        var data =
                pageData.stream().map(order -> mapper.toVoucherResponse(order)).toList();
        return PageResponse.<UserVoucherResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public PageResponse<UserVoucherResponse> getUserVouchers(int userId, int page, int size, boolean isDesc) {
        Sort sort = isDesc ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = repository.findByUser_Id(userId, pageable);
        var data =
                pageData.stream().map(order -> mapper.toVoucherResponse(order)).toList();
        return PageResponse.<UserVoucherResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public UserVoucherResponse create(int userId, String code) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var voucher =
                voucherRepository.findByCode(code).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        if (repository.existsByUser_IdAndVoucher_Code(userId, code)) throw new AppException(ErrorCode.VOUCHER_EXISTED);
        var userVoucher = ZUserVoucher.builder().user(user).voucher(voucher).build();

        return mapper.toVoucherResponse(repository.save(userVoucher));
    }
}
