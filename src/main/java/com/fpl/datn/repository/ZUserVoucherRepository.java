package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ZUserVoucher;

@Repository
public interface ZUserVoucherRepository extends JpaRepository<ZUserVoucher, Integer> {}
