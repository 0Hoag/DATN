package com.fpl.datn.repository;

import java.util.List;
import java.util.Optional;

import com.fpl.datn.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ZUserVoucher;

@Repository
public interface ZUserVoucherRepository extends JpaRepository<ZUserVoucher, Integer> {

    // Tìm theo user ID
    List<ZUserVoucher> findByUserId(Integer userId);

    // Tìm theo voucher ID
    List<ZUserVoucher> findByVoucherId(Integer voucherId);

    // Kiểm tra user đã có voucher chưa
    boolean existsByUserIdAndVoucherId(Integer userId, Integer voucherId);

    // Đếm số lượng
    long countByUserId(Integer userId);
    long countByVoucherId(Integer voucherId);

}
