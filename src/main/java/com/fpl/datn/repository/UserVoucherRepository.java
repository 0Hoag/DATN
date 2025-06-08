package com.fpl.datn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ZUserVoucher;

@Repository
public interface UserVoucherRepository extends JpaRepository<ZUserVoucher, Integer> {
    Page<ZUserVoucher> findByUser_Id(int userId, Pageable pageable);
}
