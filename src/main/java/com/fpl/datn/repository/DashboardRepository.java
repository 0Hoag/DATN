package com.fpl.datn.repository;

import com.fpl.datn.dto.response.ChartDataPoint;
import com.fpl.datn.dto.response.TopProductResponse;
import com.fpl.datn.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface  DashboardRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT COUNT(u) FROM User u")
    Long countTotalUsers();

    @Query("SELECT COUNT(o) FROM Order o")
    Long countTotalOrders();

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderStatus = 'COMPLETED'")
    BigDecimal sumTotalRevenue();

    @Query("SELECT COUNT(p) FROM Product p")
    Long countTotalProducts();

    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od")
    Long countTotalProductsSold();

    @Query("SELECT new com.fpl.datn.dto.response.TopProductResponse(p.name, SUM(od.quantity)) " +
            "FROM OrderDetail od JOIN od.product p GROUP BY p.name ORDER BY SUM(od.quantity) DESC")
    List<TopProductResponse> top10SellingProducts();

    @Query("SELECT new com.fpl.datn.dto.response.ChartDataPoint(FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m'), SUM(o.totalAmount)) " +
            "FROM Order o WHERE o.orderStatus = 'COMPLETED' GROUP BY FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m') ORDER BY FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m')")
    List<ChartDataPoint> revenueChart();

    @Query("SELECT new com.fpl.datn.dto.response.ChartDataPoint(FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m'), COUNT(o)) " +
            "FROM Order o GROUP BY FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m') ORDER BY FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m')")
    List<ChartDataPoint> orderChart();

    @Query("SELECT new com.fpl.datn.dto.response.ChartDataPoint(FUNCTION('DATE_FORMAT', od.order.createdAt, '%Y-%m'), SUM(od.quantity)) " +
            "FROM OrderDetail od GROUP BY FUNCTION('DATE_FORMAT', od.order.createdAt, '%Y-%m') ORDER BY FUNCTION('DATE_FORMAT', od.order.createdAt, '%Y-%m')")
    List<ChartDataPoint> productChart();
}
