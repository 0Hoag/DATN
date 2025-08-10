package com.fpl.datn.repository;

import java.util.List;
import java.util.Optional;

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
    Page<ZUserVoucher> findByUser_Id(int userId, Pageable pageable);

    boolean existsByUser_IdAndVoucher_Code(Integer userId, String code);

    boolean existsByVoucherId(Integer voucherId);

    // ===== NEW METHODS FOR USAGE TRACKING =====

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

    /// Phương thức để đếm số lượng voucher đã được sử dụng toàn cầu cho một voucher cụ thể
    @Query("SELECT COUNT(zuv) FROM ZUserVoucher zuv WHERE zuv.voucher.id = :voucherId AND zuv.isUsed = true")
    long countUsedVouchersByVoucherId(@Param("voucherId") Integer voucherId);

    // ĐÃ SỬA: Lấy danh sách ID của các voucher mà người dùng đã sử dụng (isUsed = true)
    @Query("SELECT zuv.voucher.id FROM ZUserVoucher zuv WHERE zuv.user.id = :userId AND zuv.isUsed = true")
    List<Integer> findFullyUsedVoucherIdsByUserId(@Param("userId") Integer userId);

    // Phương thức mới cần thêm vào
    Optional<ZUserVoucher> findByUserIdAndVoucherId(Integer userId, Integer voucherId);

    // Các phương thức khác của bạn (nếu có)
    List<ZUserVoucher> findByUserIdAndIsUsed(Integer userId, Boolean isUsed);
}
