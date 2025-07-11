package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fpl.datn.models.Order;
import com.fpl.datn.models.OrderReturn;

public interface OrderReturnRepository
        extends JpaRepository<OrderReturn, Integer>, JpaSpecificationExecutor<OrderReturn> {
    boolean existsByOrderAndStatusNot(Order order, String status);
}
