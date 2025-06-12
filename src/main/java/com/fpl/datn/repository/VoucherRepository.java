package com.fpl.datn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    // Thêm method này để tìm voucher theo code
    Optional<Voucher> findByCode(String code);

    // Kiểm tra voucher code đã tồn tại chưa
    boolean existsByCode(String code);
}
