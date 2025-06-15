package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpl.datn.models.Order;
import com.fpl.datn.models.OrderReturn;

public interface OrderReturnRepository extends JpaRepository<OrderReturn, Integer> {
    boolean existsByOrderAndStatusNot(Order order, String status);
}
