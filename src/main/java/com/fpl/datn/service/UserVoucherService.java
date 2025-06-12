package com.fpl.datn.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.UserVoucherRequest;
import com.fpl.datn.dto.response.UserVoucherResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.UserVoucherMapper;
import com.fpl.datn.models.User;
import com.fpl.datn.models.Voucher;
import com.fpl.datn.models.ZUserVoucher;
import com.fpl.datn.repository.UserRepository;
import com.fpl.datn.repository.VoucherRepository;
import com.fpl.datn.repository.ZUserVoucherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserVoucherService {
    ZUserVoucherRepository userVoucherRepository;
    UserRepository userRepository;
    VoucherRepository voucherRepository;
    UserVoucherMapper mapper;

    public PageResponse<UserVoucherResponse> getAll(int page, int size, boolean isDesc) {
        Sort sort = isDesc ? Sort.by(Sort.Direction.DESC, "id") : Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        var pageData = userVoucherRepository.findAll(pageable);
        var data = pageData.stream().map(mapper::toUserVoucherResponse).toList();

        return PageResponse.<UserVoucherResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    public UserVoucherResponse getUserVoucher(int id) {
        var userVoucher = userVoucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_VOUCHER_NOT_FOUND));
        return mapper.toUserVoucherResponse(userVoucher);
    }

    @Transactional
    public UserVoucherResponse assignVoucherToUser(UserVoucherRequest request) {
        // Kiểm tra user tồn tại
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Kiểm tra voucher tồn tại
        Voucher voucher = voucherRepository.findById(request.getVoucherId())
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        // Kiểm tra user đã có voucher này chưa (bằng cách tìm trong tất cả records)
        List<ZUserVoucher> allUserVouchers = userVoucherRepository.findAll();
        boolean exists = allUserVouchers.stream()
                .anyMatch(uv -> uv.getUser().getId().equals(request.getUserId())
                        && uv.getVoucher().getId().equals(request.getVoucherId()));

        if (exists) {
            throw new AppException(ErrorCode.USER_VOUCHER_ALREADY_EXISTS);
        }

        // Tạo mới user-voucher
        ZUserVoucher userVoucher = new ZUserVoucher();
        userVoucher.setUser(user);
        userVoucher.setVoucher(voucher);

        userVoucher = userVoucherRepository.save(userVoucher);
        return mapper.toUserVoucherResponse(userVoucher);
    }

    @Transactional
    public void delete(int id) {
        if (!userVoucherRepository.existsById(id)) {
            throw new AppException(ErrorCode.USER_VOUCHER_NOT_FOUND);
        }
        userVoucherRepository.deleteById(id);
    }

    public boolean checkUserHasVoucher(int userId, int voucherId) {
        List<ZUserVoucher> allUserVouchers = userVoucherRepository.findAll();
        return allUserVouchers.stream()
                .anyMatch(uv -> uv.getUser().getId().equals(userId)
                        && uv.getVoucher().getId().equals(voucherId));
    }

    public List<UserVoucherResponse> getUserVouchersByUserId(int userId) {
        // Kiểm tra user tồn tại
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        List<ZUserVoucher> allUserVouchers = userVoucherRepository.findAll();
        return allUserVouchers.stream()
                .filter(uv -> uv.getUser().getId().equals(userId))
                .map(mapper::toUserVoucherResponse)
                .toList();
    }

    public List<UserVoucherResponse> getUserVouchersByVoucherId(int voucherId) {
        // Kiểm tra voucher tồn tại
        if (!voucherRepository.existsById(voucherId)) {
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }

        List<ZUserVoucher> allUserVouchers = userVoucherRepository.findAll();
        return allUserVouchers.stream()
                .filter(uv -> uv.getVoucher().getId().equals(voucherId))
                .map(mapper::toUserVoucherResponse)
                .toList();
    }

    public long countUsersByVoucher(int voucherId) {
        if (!voucherRepository.existsById(voucherId)) {
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }

        List<ZUserVoucher> allUserVouchers = userVoucherRepository.findAll();
        return allUserVouchers.stream()
                .filter(uv -> uv.getVoucher().getId().equals(voucherId))
                .count();
    }

    public long countVouchersByUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        List<ZUserVoucher> allUserVouchers = userVoucherRepository.findAll();
        return allUserVouchers.stream()
                .filter(uv -> uv.getUser().getId().equals(userId))
                .count();
    }
}
