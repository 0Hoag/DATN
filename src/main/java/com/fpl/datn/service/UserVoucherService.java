package com.fpl.datn.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fpl.datn.models.User;
import com.fpl.datn.models.ZUserVoucher;
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

    // CHỨC NĂNG 1: ADMIN PHÁT VOUCHER CHO NHIỀU USER
    @Transactional
    public List<UserVoucherResponse> bulkAssignVoucherToUsers(UserVoucherRequest request) {
        var voucher = voucherRepository.findById(request.getVoucherId())
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        validateVoucherForAssignment(voucher);

        List<UserVoucherResponse> results = new ArrayList<>();

        for (Integer userId : request.getUserIds()) {
            try {
                var user = userRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                if (userVoucherRepository.existsByUserIdAndVoucherId(userId, request.getVoucherId())) {
                    continue;
                }

                var userVoucher = new com.fpl.datn.models.ZUserVoucher();
                userVoucher.setUser(user);
                userVoucher.setVoucher(voucher);

                userVoucherRepository.save(userVoucher);
                results.add(mapper.toUserVoucherResponse(userVoucher));

            } catch (Exception e) {
                continue;
            }
        }

        return results;
    }

    // CHỨC NĂNG 2: USER TỰ NHẬN VOUCHER
    @Transactional
    public UserVoucherResponse claimVoucher(UserVoucherRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var voucher = voucherRepository.findByCode(request.getVoucherCode())
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        validateVoucherForClaim(voucher);

        if (userVoucherRepository.existsByUserIdAndVoucherId(request.getUserId(), voucher.getId())) {
            throw new AppException(ErrorCode.USER_VOUCHER_ALREADY_EXISTS);
        }

        if (voucher.getQuantity() != -1) {
            long currentUsage = userVoucherRepository.countByVoucherId(voucher.getId());
            if (currentUsage >= voucher.getQuantity()) {
                throw new AppException(ErrorCode.VOUCHER_OUT_OF_STOCK);
            }
        }

        var userVoucher = new com.fpl.datn.models.ZUserVoucher();
        userVoucher.setUser(user);
        userVoucher.setVoucher(voucher);

        userVoucherRepository.save(userVoucher);
        return mapper.toUserVoucherResponse(userVoucher);
    }

    @Transactional
    public UserVoucherResponse assignVoucherToUser(UserVoucherRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var voucher = voucherRepository.findById(request.getVoucherId())
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        if (userVoucherRepository.existsByUserIdAndVoucherId(request.getUserId(), request.getVoucherId())) {
            throw new AppException(ErrorCode.USER_VOUCHER_ALREADY_EXISTS);
        }

        var userVoucher = new com.fpl.datn.models.ZUserVoucher();
        userVoucher.setUser(user);
        userVoucher.setVoucher(voucher);

        userVoucherRepository.save(userVoucher);
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
        return userVoucherRepository.existsByUserIdAndVoucherId(userId, voucherId);
    }

    public List<UserVoucherResponse> getUserVouchersByUserId(int userId, boolean desc) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        var userVouchers = userVoucherRepository.findByUserId(userId);
        return userVouchers.stream()
                .map(mapper::toUserVoucherResponse)
                .toList();
    }

    public List<UserVoucherResponse> getUserVouchersByVoucherId(int voucherId, boolean desc) {
        if (!voucherRepository.existsById(voucherId)) {
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }

        var userVouchers = userVoucherRepository.findByVoucherId(voucherId);
        return userVouchers.stream()
                .map(mapper::toUserVoucherResponse)
                .toList();
    }

    public long countUsersByVoucher(int voucherId) {
        if (!voucherRepository.existsById(voucherId)) {
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }
        return userVoucherRepository.countByVoucherId(voucherId);
    }

    public long countVouchersByUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return userVoucherRepository.countByUserId(userId);
    }

    private void validateVoucherForAssignment(com.fpl.datn.models.Voucher voucher) {
        if (!voucher.getIsActive()) {
            throw new AppException(ErrorCode.VOUCHER_INACTIVE);
        }

        var now = LocalDateTime.now();
        if (voucher.getStartAt().isAfter(now)) {
            throw new AppException(ErrorCode.VOUCHER_NOT_STARTED);
        }

        if (voucher.getEndAt().isBefore(now)) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }
    }

    private void validateVoucherForClaim(com.fpl.datn.models.Voucher voucher) {
        validateVoucherForAssignment(voucher);
    }
    // Thêm vào UserVoucherService
    @Transactional
    public List<UserVoucherResponse> assignVoucherToAllUsers(UserVoucherRequest request) {
        var voucher = voucherRepository.findById(request.getVoucherId())
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        validateVoucherForAssignment(voucher);

        // SỬA DÒNG NÀY - Lấy tất cả user rồi filter những user active
        List<User> allActiveUsers = userRepository.findAll()
                .stream()
                .filter(user -> user.getIsEnable() != null && user.getIsEnable())
                .toList();

        List<UserVoucherResponse> results = new ArrayList<>();

        for (User user : allActiveUsers) {
            try {
                // Kiểm tra user đã có voucher này chưa
                if (userVoucherRepository.existsByUserIdAndVoucherId(user.getId(), request.getVoucherId())) {
                    continue; // Bỏ qua nếu đã có
                }

                var userVoucher = new ZUserVoucher();
                userVoucher.setUser(user);
                userVoucher.setVoucher(voucher);

                userVoucherRepository.save(userVoucher);
                results.add(mapper.toUserVoucherResponse(userVoucher));

            } catch (Exception e) {
                // Log lỗi nhưng tiếp tục với user khác
                continue;
            }
        }

        return results;
    }
}
