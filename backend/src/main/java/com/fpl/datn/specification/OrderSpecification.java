package com.fpl.datn.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.fpl.datn.models.Order;

public class OrderSpecification {

    // tìm theo id đơn hàng
    public static Specification<Order> hasId(Integer id) {
        return (root, query, cb) -> {
            if (id == null) {
                return null;
            }
            return cb.equal(root.get("id"), id);
        };
    }
    // tìm theo tên user
    public static Specification<Order> hasFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("user").get("fullName")), "%" + fullName.toLowerCase() + "%");
        };
    }
    // tìm theo số điện thoại
    public static Specification<Order> hasPhone(String phone) {
        return (root, query, cb) -> {
            if (phone == null || phone.isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("user").get("phone")), "%" + phone.toLowerCase() + "%");
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
            if (orderStatus == null) {
                return null;
            }
            return cb.equal(root.get("orderStatus"), orderStatus);
        };
    }
    // tìm theo trạng thái thanh toán
    public static Specification<Order> hasPaymentStatus(String paymentSatus) {
        return (root, query, cb) -> {
            if (paymentSatus == null) {
                return null;
            }
            return cb.equal(root.get("paymentStatus"), paymentSatus);
        };
    }
}
