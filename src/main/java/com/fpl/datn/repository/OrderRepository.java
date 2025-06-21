package com.fpl.datn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, JpaSpecificationExecutor<Order> {
    boolean existsByOrderStatus(String orderStatus);

    Page<Order> findByIsDeleteFalse(Pageable pageable);

    boolean existsByIdAndIsDeleteTrue(int id);
}
