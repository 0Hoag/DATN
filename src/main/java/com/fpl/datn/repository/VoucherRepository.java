package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {}
