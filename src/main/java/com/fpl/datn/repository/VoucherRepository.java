package com.fpl.datn.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    // Tìm voucher theo code
    Optional<Voucher> findByCode(String code);

    // Kiểm tra voucher code đã tồn tại chưa
    boolean existsByCode(String code);

    // Search voucher theo code hoặc description
    Page<Voucher> findByCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String code, String description, Pageable pageable);

    // Tìm voucher đang hoạt động nhưng đã hết hạn
    List<Voucher> findByIsActiveTrueAndEndAtBefore(LocalDateTime dateTime);

    // Đếm voucher đang hoạt động nhưng đã hết hạn
    long countByIsActiveTrueAndEndAtBefore(LocalDateTime dateTime);

    // Tìm voucher sắp hết hạn (trong khoảng thời gian)
    Page<Voucher> findByIsActiveTrueAndEndAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Tìm voucher đang hoạt động
    Page<Voucher> findByIsActiveTrue(Pageable pageable);

    // Tìm voucher đã ngừng hoạt động
    Page<Voucher> findByIsActiveFalse(Pageable pageable);
}
