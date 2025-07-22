package com.fpl.datn.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ZUserVoucher;

@Repository
public interface UserVoucherRepository extends JpaRepository<ZUserVoucher, Integer> {

    // ===== EXISTING METHODS =====

    // ===== EXISTING METHODS =====
    Page<ZUserVoucher> findByUser_Id(int userId, Pageable pageable);

    boolean existsByUser_IdAndVoucher_Code(Integer userId, String code);

    boolean existsByVoucherId(Integer voucherId);

    // Đếm số record của user với voucher ID
    @Query("SELECT COUNT(uv) FROM ZUserVoucher uv WHERE uv.user.id = :userId AND uv.voucher.id = :voucherId")
    long countByUser_IdAndVoucher_Id(@Param("userId") Integer userId, @Param("voucherId") Integer voucherId);

    // Đếm tổng số record của voucher
    @Query("SELECT COUNT(uv) FROM ZUserVoucher uv WHERE uv.voucher.id = :voucherId")
    long countByVoucher_Id(@Param("voucherId") Integer voucherId);

    // Lấy danh sách user đã claim voucher
    @Query("SELECT DISTINCT uv.user.id FROM ZUserVoucher uv WHERE uv.voucher.id = :voucherId")
    List<Integer> findUserIdsByVoucherId(@Param("voucherId") Integer voucherId);

    // Lấy danh sách voucher của user
    @Query("SELECT uv FROM ZUserVoucher uv WHERE uv.user.id = :userId")
    List<ZUserVoucher> findAllByUserId(@Param("userId") Integer userId);
}
