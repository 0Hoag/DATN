package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpl.datn.models.TransactionLog;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Integer> {
    TransactionLog findFirstByOrderIdAndActionTypeOrderByCreatedAtDesc(int oderId, String typeAction);
}
