package com.fpl.datn.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fpl.datn.models.User;
import com.fpl.datn.models.Voucher;
import com.fpl.datn.models.ZUserVoucher;
import com.fpl.datn.repository.UserVoucherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoucherUsageService {

    UserVoucherRepository userVoucherRepository;

    // Ghi lại việc claim voucher
    @Transactional
    public void recordVoucherClaim(User user, Voucher voucher) {
        ZUserVoucher userVoucher =
                ZUserVoucher.builder().user(user).voucher(voucher).build();

        userVoucherRepository.save(userVoucher);
        log.info("Recorded voucher claim: User {} claimed voucher {}", user.getId(), voucher.getCode());
    }

    // Ghi lại việc sử dụng voucher (tạo record thứ 2)
    @Transactional
    public void recordVoucherUsage(User user, Voucher voucher) {
        // Tạo record thứ 2 để đánh dấu đã sử dụng
        ZUserVoucher usageRecord =
                ZUserVoucher.builder().user(user).voucher(voucher).build();

        userVoucherRepository.save(usageRecord);
        log.info("Recorded voucher usage: User {} used voucher {}", user.getId(), voucher.getCode());
    }

    // Kiểm tra user có thể sử dụng voucher không
    public boolean canUserUseVoucher(Integer userId, Integer voucherId) {
        // Đếm số record của user với voucher này
        long recordCount = userVoucherRepository.countByUser_IdAndVoucher_Id(userId, voucherId);

        if (recordCount == 0) {
            log.warn("User {} has not claimed voucher {}", userId, voucherId);
            return false; // Chưa claim
        }

        if (recordCount >= 2) {
            log.warn("User {} has already used voucher {} (records: {})", userId, voucherId, recordCount);
            return false; // Đã sử dụng rồi
        }

        log.info("User {} can use voucher {} (records: {})", userId, voucherId, recordCount);
        return true; // Đã claim nhưng chưa sử dụng
    }

    // Kiểm tra user đã claim voucher chưa
    public boolean hasUserClaimedVoucher(Integer userId, Integer voucherId) {
        return userVoucherRepository.countByUser_IdAndVoucher_Id(userId, voucherId) > 0;
    }

    // Kiểm tra user đã sử dụng voucher chưa
    public boolean hasUserUsedVoucher(Integer userId, Integer voucherId) {
        return userVoucherRepository.countByUser_IdAndVoucher_Id(userId, voucherId) >= 2;
    }

    // Thống kê số lần voucher được claim
    public long getVoucherClaimCount(Integer voucherId) {
        return userVoucherRepository.countByVoucher_Id(voucherId);
    }

    // Thống kê số lần voucher được sử dụng (chia đôi vì mỗi lần dùng = 2 records)
    public long getVoucherUsageCount(Integer voucherId) {
        long totalRecords = userVoucherRepository.countByVoucher_Id(voucherId);
        // Mỗi user claim = 1 record, mỗi user sử dụng = thêm 1 record nữa
        // Nên số lần sử dụng thực tế khó tính chính xác, cần logic phức tạp hơn
        return totalRecords / 2; // Ước tính thô
    }
}
