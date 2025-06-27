package com.fpl.datn.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpl.datn.dto.response.ChartPointIntResponse;
import com.fpl.datn.dto.response.ChartPointResponse;
import com.fpl.datn.dto.response.TopProductResponse;
import com.fpl.datn.models.Order;

@Repository
public interface DashboardRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT COUNT(u.id) FROM User u")
    long countTotalUsers();

    @Query("SELECT COUNT(o) FROM Order o")
    long countTotalOrders();

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransactionLog t WHERE t.order.paymentStatus = 'PAID'")
    BigDecimal sumTotalRevenue();

    @Query(
            "SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od WHERE od.order.paymentStatus = 'PAID' AND MONTH(od.order.createdAt) = MONTH(CURRENT_DATE()) AND YEAR(od.order.createdAt) = YEAR(CURRENT_DATE())")
    long countTotalProductsSold();

    @Query(
            value =
                    """
	SELECT CONCAT(p.name, ' - ', pv.variant_name), p.thumbnail, SUM(pv.sold), MONTH(pv.created_at)
	FROM product_variants pv
	JOIN products p ON pv.product_id = p.id
	WHERE pv.is_active = 1
	AND MONTH(pv.created_at) = :month
	AND YEAR(pv.created_at) = :year
	GROUP BY p.id, p.name, p.thumbnail, pv.id, pv.variant_name, MONTH(pv.created_at)
	ORDER BY SUM(pv.sold) DESC
	LIMIT 10
	""",
            nativeQuery = true)
    List<Object[]> findTop10ProductsSoldByMonthYear(@Param("month") int month, @Param("year") int year);

    // Convert method cho native query
    default List<TopProductResponse> findTop10ProductsSoldByMonthYearDto(int month, int year) {
        return findTop10ProductsSoldByMonthYear(month, year).stream()
                .map(r -> TopProductResponse.builder()
                        .productName((String) r[0])
                        .thumbnail((String) r[1])
                        .quantitySold(((Number) r[2]).longValue())
                        .soldMonth(((Number) r[3]).intValue())
                        .soldYear(year)
                        .build())
                .toList();
    }

    @Query(
            value =
                    """
		SELECT DATE_FORMAT(t.created_at, '%Y-%m') AS date, pm.name AS name, SUM(t.amout) AS value
		FROM transaction_logs t
		JOIN payment_methods pm ON t.payment_method_id = pm.id
		GROUP BY date, pm.name
		ORDER BY date
		""",
            nativeQuery = true)
    List<Object[]> getRevenueChartNative();

    default List<ChartPointResponse> getRevenueChart() {
        return getRevenueChartNative().stream()
                .map(row -> new ChartPointResponse((String) row[0], (String) row[1], (BigDecimal) row[2]))
                .collect(Collectors.toList());
    }

    @Query(
            value =
                    """
		SELECT DATE_FORMAT(o.created_at, '%Y-%m') AS date, o.order_status AS name, COUNT(o.id) AS value
		FROM orders o
		GROUP BY date, o.order_status
		ORDER BY date
		""",
            nativeQuery = true)
    List<Object[]> getOrderChartNative();

    default List<ChartPointIntResponse> getOrderChart() {
        return getOrderChartNative().stream()
                .map(row -> new ChartPointIntResponse((String) row[0], (String) row[1], ((Number) row[2]).longValue()))
                .collect(Collectors.toList());
    }

    @Query(
            value =
                    """
		SELECT DATE_FORMAT(o.created_at, '%Y-%m') AS date, p.name, SUM(od.quantity) AS value
		FROM order_details od
		JOIN orders o ON od.order_id = o.id
		JOIN products p ON od.product_id = p.id
		GROUP BY date, p.name
		ORDER BY date
		""",
            nativeQuery = true)
    List<Object[]> getProductChartNative();

    default List<ChartPointIntResponse> getProductChart() {
        return getProductChartNative().stream()
                .map(row -> new ChartPointIntResponse((String) row[0], (String) row[1], ((Number) row[2]).longValue()))
                .collect(Collectors.toList());
    }
}
