package com.fpl.datn.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.fpl.datn.models.OrderReturn;

public class OrderReturnSpecification {

    // tìm theo keyword phone, id,fullname
    public static Specification<OrderReturn> hasFullName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return null;
            }
            String likeKeyword = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("user").get("fullName")), likeKeyword),
                    cb.like(cb.toString(root.get("id")), keyword.toLowerCase()));
        };
    }
    // tìm theo ngày
    public static Specification<OrderReturn> createAtBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null || endDate == null) {
                return null;
            }
            return cb.between(root.get("createdAt"), startDate, endDate);
        };
    }
    // tìm theo trạng thái đơn hàng
    public static Specification<OrderReturn> hasOrderReturnStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.trim().isEmpty()) {
                return null;
            }
            return cb.equal(root.get("status"), status);
        };
    }
}
