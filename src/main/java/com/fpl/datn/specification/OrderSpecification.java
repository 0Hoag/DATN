package com.fpl.datn.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.fpl.datn.models.Order;

public class OrderSpecification {

    // tìm theo keyword phone, id,fullname
    public static Specification<Order> hasFullName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return null;
            }
            String likeKeyword = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("user").get("fullName")), likeKeyword),
                    cb.like(cb.lower(root.get("user").get("phone")), likeKeyword),
                    cb.like(cb.toString(root.get("id")), likeKeyword));
        };
    }
    // tìm theo ngày
    public static Specification<Order> createAtBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null || endDate == null) {
                return null;
            }
            return cb.between(root.get("createdAt"), startDate, endDate);
        };
    }
    // tìm theo trạng thái đơn hàng
    public static Specification<Order> hasOrderStatus(String orderStatus) {
        return (root, query, cb) -> {
            if (orderStatus == null || orderStatus.trim().isEmpty()) {
                return null;
            }
            return cb.equal(root.get("orderStatus"), orderStatus);
        };
    }
    // tìm theo trạng thái thanh toán
    public static Specification<Order> hasPaymentStatus(String paymentSatus) {
        return (root, query, cb) -> {
            if (paymentSatus == null || paymentSatus.trim().isEmpty()) {
                return null;
            }
            return cb.equal(root.get("paymentStatus"), paymentSatus);
        };
    }
}
