package com.fpl.datn.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpl.datn.constant.PredefinedRole;
import com.fpl.datn.dto.PageResponse;
import com.fpl.datn.dto.request.UserVoucherClaimRequest;
import com.fpl.datn.dto.request.ZUserVoucherAssignAllRequest;
import com.fpl.datn.dto.response.VoucherResponse;
import com.fpl.datn.dto.response.ZUserVoucherResponse;
import com.fpl.datn.exception.AppException;
import com.fpl.datn.exception.ErrorCode;
import com.fpl.datn.mapper.UserVoucherMapper;
import com.fpl.datn.mapper.VoucherMapper;
import com.fpl.datn.models.User;
import com.fpl.datn.models.Voucher;
import com.fpl.datn.models.ZUserVoucher;
import com.fpl.datn.repository.UserRepository;
import com.fpl.datn.repository.UserVoucherRepository;
import com.fpl.datn.repository.VoucherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserVoucherService {
    UserVoucherRepository repository;
    UserVoucherMapper zUserVoucherMapper;
    VoucherRepository voucherRepository;
    UserRepository userRepository;
    UserService userService;
    AuthenticationService authenticationService;
    UserVoucherMapper mapper;
    VoucherMapper voucherMapper;

    @PreAuthorize("hasAuthority('VIEW_USER_VOUCHER') or hasRole('ADMIN')")
    public PageResponse<ZUserVoucherResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repository.findAll(pageable);
        var data = pageData.stream()
                .map(zUserVoucherMapper::toZUserVoucherResponse)
                .toList();
        return PageResponse.<ZUserVoucherResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }

    @PreAuthorize(
            "hasAuthority('VIEW_USER_VOUCHER') or hasRole('ADMIN') or hasRole('" + PredefinedRole.ROLE_USER + "')")
    public ZUserVoucherResponse getZUserVoucher(int id) {
        var zUserVoucher = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        return zUserVoucherMapper.toZUserVoucherResponse(zUserVoucher);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ASSIGN_VOUCHER') or hasRole('ADMIN')")
    public void assignVoucherToAllUsers(ZUserVoucherAssignAllRequest request) {
        Voucher voucher = voucherRepository
                .findById(request.getVoucherId())
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        if (!isVoucherGloballyAvailable(voucher)) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }
        List<User> users = userRepository.findAll().stream()
                .filter(user ->
                        user.getRoles().stream().anyMatch(role -> role.getName().equals(PredefinedRole.ROLE_USER)))
                .collect(Collectors.toList());
        for (User user : users) {
            // Kiểm tra xem người dùng đã có voucher này chưa (dựa trên cặp userId và voucherId)
            if (repository
                    .findByUserIdAndVoucherId(user.getId(), voucher.getId())
                    .isEmpty()) {
                ZUserVoucher zUserVoucher = ZUserVoucher.builder()
                        .user(user)
                        .voucher(voucher)
                        .isUsed(false) // Mặc định là chưa sử dụng khi gán
                        .assignedAt(LocalDateTime.now())
                        .build();
                repository.save(zUserVoucher);
            }
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('CLAIM_VOUCHER') or hasRole('" + PredefinedRole.ROLE_USER + "')")
    public ZUserVoucherResponse claimVoucher(UserVoucherClaimRequest request) {
        var currentUserResponse = userService.getMyInfo();
        User currentUser = userRepository
                .findById(currentUserResponse.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Voucher voucher = voucherRepository
                .findByCode(request.getVoucherCode())
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        if (!isVoucherGloballyAvailable(voucher)) {
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }
        Optional<ZUserVoucher> existingUserVoucher =
                repository.findByUserIdAndVoucherId(currentUser.getId(), voucher.getId());
        if (existingUserVoucher.isPresent()) {
            // Nếu người dùng đã có voucher này
            if (!existingUserVoucher.get().getIsUsed()) {
                // Nếu voucher chưa được sử dụng, trả về bản ghi hiện có (người dùng đã claim)
                return zUserVoucherMapper.toZUserVoucherResponse(existingUserVoucher.get());
            } else {
                // Nếu voucher đã được sử dụng, ném lỗi (người dùng đã dùng voucher này rồi)
                throw new AppException(
                        ErrorCode.VOUCHER_EXISTED); // Có thể đổi thành VOUCHER_OVERUSED hoặc VOUCHER_ALREADY_USED
            }
        } else {
            // Nếu người dùng chưa có voucher này, tạo bản ghi mới
            ZUserVoucher zUserVoucher = ZUserVoucher.builder()
                    .user(currentUser)
                    .voucher(voucher)
                    .isUsed(false) // Mặc định là chưa sử dụng khi claim
                    .assignedAt(LocalDateTime.now())
                    .build();
            return zUserVoucherMapper.toZUserVoucherResponse(
                    repository.save(zUserVoucher)); // Đã sửa từ toUserVoucherResponse
        }
    }

    @PreAuthorize("hasAuthority('DELETE_USER_VOUCHER') or hasRole('ADMIN')")
    public void deleteZUserVoucher(int id) {
        if (!repository.existsById(id)) {
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }
        var zUserVoucher = repository.findById(id).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        if (zUserVoucher.getIsUsed()) {
            throw new AppException(ErrorCode.INVALID_INPUT); // Không thể xóa voucher đã sử dụng
        }
        repository.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('" + PredefinedRole.ROLE_USER + "', 'ADMIN', 'MANAGER')")
    public long getAvailableVoucherCountForCurrentUser() {
        var currentUserResponse = userService.getMyInfo();
        User currentUser = userRepository
                .findById(currentUserResponse.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        LocalDateTime now = LocalDateTime.now();
        // Lấy các voucher mà người dùng đã nhận nhưng chưa sử dụng
        List<ZUserVoucher> userUnusedVouchers = repository.findByUserIdAndIsUsed(currentUser.getId(), false);
        long count = 0;
        for (ZUserVoucher zuv : userUnusedVouchers) {
            Voucher voucher = zuv.getVoucher();
            // Kiểm tra xem voucher đó có còn khả dụng trên toàn hệ thống không
            if (isVoucherGloballyAvailable(voucher)) {
                count++;
            }
        }
        return count;
    }

    private boolean isVoucherGloballyAvailable(Voucher voucher) {
        LocalDateTime now = LocalDateTime.now();
        // Kiểm tra voucher có đang hoạt động, còn hạn không
        if (!voucher.getIsActive()
                || voucher.getStartAt().isAfter(now)
                || voucher.getEndAt().isBefore(now)) {
            return false;
        }
        // Kiểm tra số lượng voucher đã được sử dụng toàn cầu (dựa trên isUsed = true)
        long usedCount = repository.countUsedVouchersByVoucherId(voucher.getId());
        if (voucher.getQuantity() != null && usedCount >= voucher.getQuantity()) {
            return false;
        }
        return true;
    }

    // PHƯƠNG THỨC MỚI: Lấy danh sách voucher mà người dùng có thể sử dụng
    public PageResponse<VoucherResponse> getVouchersUserCanUse(int page, int size) {
        var userId = authenticationService.extractUserIdFromSecurityContext();
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Pageable pageable = PageRequest.of(page - 1, size);
        var pageData = repository.findAvailableVouchersForUser(user.getId(), pageable);
        var data = pageData.stream()
                .map(userVoucher -> mapper.UserVoucherResponse(userVoucher))
                .toList();
        return PageResponse.<VoucherResponse>builder()
                .currentPage(page)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(data)
                .build();
    }
}
