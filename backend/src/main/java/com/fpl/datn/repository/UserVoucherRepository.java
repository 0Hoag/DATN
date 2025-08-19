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

    // Lấy danh sách voucher đã lưu của một user (có phân trang)
    Page<ZUserVoucher> findByUser_Id(int userId, Pageable pageable);

    // Kiểm tra xem một user đã lưu một voucher cụ thể chưa
    boolean existsByUser_IdAndVoucher_Code(Integer userId, String code);

    // Kiểm tra xem một voucher có đang được sử dụng hay không
    boolean existsByVoucherId(Integer voucherId);

    // Đếm số record của user với voucher ID
    @Query("SELECT COUNT(uv) FROM ZUserVoucher uv WHERE uv.user.id = :userId AND uv.voucher.id = :voucherId")
    long countByUser_IdAndVoucher_Id(@Param("userId") Integer userId, @Param("voucherId") Integer voucherId);

    // Đếm tổng số record của voucher (ĐÃ KHÔI PHỤC)
    @Query("SELECT COUNT(uv) FROM ZUserVoucher uv WHERE uv.voucher.id = :voucherId")
    long countByVoucher_Id(@Param("voucherId") Integer voucherId);

    // Đếm số lượng voucher đã được sử dụng toàn cầu
    @Query("SELECT COUNT(zuv) FROM ZUserVoucher zuv WHERE zuv.voucher.id = :voucherId AND zuv.isUsed = true")
    long countUsedVouchersByVoucherId(@Param("voucherId") Integer voucherId);

    // Lấy danh sách các voucher khả dụng cho người dùng (có Join Fetch để tối ưu)
    @Query(
            """
          SELECT uv
          FROM ZUserVoucher uv
          JOIN FETCH uv.voucher v
          WHERE uv.user.id = :userId
          AND uv.isUsed = false
          AND CURRENT_TIMESTAMP < v.endAt
    """)
    Page<ZUserVoucher> findAvailableVouchersForUser(@Param("userId") Integer userId, Pageable pageable);

    // Tìm một bản ghi ZUserVoucher dựa trên ID người dùng và ID voucher
    Optional<ZUserVoucher> findByUserIdAndVoucherId(Integer userId, Integer voucherId);

    // Lấy danh sách voucher của một user theo trạng thái đã dùng hay chưa
    List<ZUserVoucher> findByUserIdAndIsUsed(Integer userId, Boolean isUsed);
}