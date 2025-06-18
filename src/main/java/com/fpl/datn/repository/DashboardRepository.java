package com.fpl.datn.repository;

import com.fpl.datn.dto.response.TopProductResponse;
import com.fpl.datn.dto.response.RevenueChartResponse;
import com.fpl.datn.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface DashboardRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT COUNT(u) FROM User u")
    long countTotalUsers();

    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionLog t WHERE t.order.paymentStatus = 'PAID'")
    BigDecimal sumTotalRevenue();

    @Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od WHERE od.order.paymentStatus = 'PAID'")
    long countTotalProductsSold();

    @Query(value = "SELECT " +
            " CONCAT(p.name, ' - ', pv.variant_name) as productName, " +
            " p.thumbnail, " +
            " COALESCE(SUM(od.quantity), 0) as quantitySold, " +
            " COALESCE(SUM(od.quantity * od.price), 0) as totalRevenue " +
            "FROM product_variants pv " +
            "JOIN products p ON pv.product_id = p.id " +
            "LEFT JOIN order_details od ON pv.id = od.product_variant_id " +
            "LEFT JOIN orders o ON od.order_id = o.id " +
            "WHERE pv.is_active = 1 AND (o.payment_status = 'PAID' OR o.payment_status IS NULL) " +
            "GROUP BY p.id, p.name, p.thumbnail, pv.id, pv.variant_name " +
            "ORDER BY COALESCE(SUM(od.quantity), 0) DESC " +
            "LIMIT 10",
            nativeQuery = true)
    List<Object[]> findTop10ProductsNative();

    // Convert method cho native query
    default List<TopProductResponse> findTop10Products() {
        return findTop10ProductsNative().stream()
                .map(row -> TopProductResponse.builder()
                        .productName((String) row[0])
                        .thumbnail((String) row[1])
                        .quantitySold(((Number) row[2]).longValue())
                        .totalRevenue((BigDecimal) row[3])
                        .build())
                .collect(Collectors.toList());
    }

    //Thống kê doanh thu theo từng ngày
    @Query(value = "SELECT " +
            " DATE_FORMAT(t.created_at, '%Y-%m-%d') as date, " +
            " COUNT(DISTINCT t.order_id) as orderCount, " +
            " COALESCE(SUM(od_sum.total_quantity), 0) as productsSold, " +
            " COALESCE(SUM(t.amout), 0) as totalRevenue " +
            "FROM transaction_logs t " +
            "LEFT JOIN orders o ON t.order_id = o.id " +
            "LEFT JOIN (" +
            "   SELECT order_id, SUM(quantity) as total_quantity " +
            "   FROM order_details " +
            "   GROUP BY order_id" +
            ") od_sum ON o.id = od_sum.order_id " +
            "WHERE t.status = 1 AND o.payment_status = 'PAID' " +
            "GROUP BY DATE_FORMAT(t.created_at, '%Y-%m-%d') " +
            "ORDER BY DATE_FORMAT(t.created_at, '%Y-%m-%d') ASC",
            nativeQuery = true)
    List<Object[]> getDailyChartNative();

    default List<RevenueChartResponse> getDailyChart() {
        return getDailyChartNative().stream()
                .map(row -> new RevenueChartResponse(
                        (String) row[0],           // date
                        ((Number) row[1]).longValue(),     // orderCount
                        ((Number) row[2]).longValue(),     // productsSold
                        (BigDecimal) row[3]        // totalRevenue
                ))
                .collect(Collectors.toList());
    }
}
