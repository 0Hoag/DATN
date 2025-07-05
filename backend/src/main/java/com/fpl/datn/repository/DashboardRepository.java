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

	@Query("SELECT COALESCE(SUM(od.quantity), 0) FROM OrderDetail od WHERE od.order.paymentStatus = 'PAID' AND YEAR(od.order.createdAt) = :year")
	long countTotalProductsSold(@Param("year") int year);

	@Query(
			value =
					"""
        SELECT CONCAT(p.name, ' - ', pv.variant_name) AS product_name, p.thumbnail, SUM(pv.sold) AS quantity_sold, DATE_FORMAT(pv.created_at, '%Y-%m') AS date
        FROM product_variants pv
        JOIN products p ON pv.product_id = p.id
        WHERE pv.is_active = 1
        AND YEAR(pv.created_at) = :year
        GROUP BY DATE_FORMAT(pv.created_at, '%Y-%m'), p.id, p.name, p.thumbnail, pv.id, pv.variant_name
        ORDER BY SUM(pv.sold) DESC
        LIMIT 10
        """,
			nativeQuery = true)
	List<Object[]> findTop10ProductsSoldByYear(@Param("year") int year);

	default List<TopProductResponse> findTop10ProductsSoldByYearDto(int year) {
		return findTop10ProductsSoldByYear(year).stream()
				.map(r -> TopProductResponse.builder()
						.productName((String) r[0])
						.thumbnail((String) r[1])
						.quantitySold(((Number) r[2]).longValue())
						.date((String) r[3]) // Map trường date
						.year(year)
						.build())
				.toList();
	}

	@Query(
			value = """
    SELECT DATE_FORMAT(t.created_at, '%Y-%m') AS date, 'Total Revenue' AS name, SUM(t.amout) AS value
    FROM transaction_logs t
    WHERE YEAR(t.created_at) = :year
    GROUP BY DATE_FORMAT(t.created_at, '%Y-%m')
    ORDER BY date
    """,
			nativeQuery = true)
	List<Object[]> getRevenueChartNative(@Param("year") int year);

	default List<ChartPointResponse> getRevenueChart(int year) {
		return getRevenueChartNative(year).stream()
				.map(row -> ChartPointResponse.builder()
						.date((String) row[0])
						.name((String) row[1])
						.value(row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO) // Chuyển đổi an toàn
						.year(year)
						.build())
				.toList();
	}

	@Query(
			value = """
        SELECT DATE_FORMAT(o.created_at, '%Y-%m') AS date, o.order_status AS name, COUNT(o.id) AS value
        FROM orders o
        WHERE YEAR(o.created_at) = :year
        GROUP BY DATE_FORMAT(o.created_at, '%Y-%m'), o.order_status
        ORDER BY date
        """,
			nativeQuery = true)
	List<Object[]> getOrderChartNative(@Param("year") int year);

	default List<ChartPointIntResponse> getOrderChart(int year) {
		return getOrderChartNative(year).stream()
				.map(row -> ChartPointIntResponse.builder()
						.date((String) row[0])
						.name((String) row[1])
						.value(row[2] != null ? Long.parseLong(row[2].toString()) : 0L)
						.year(year)
						.build())
				.toList();
	}

	@Query(
			value = """
        SELECT DATE_FORMAT(o.created_at, '%Y-%m') AS date, p.name, SUM(od.quantity) AS value
        FROM order_details od
        JOIN orders o ON od.order_id = o.id
        JOIN products p ON od.product_id = p.id
        WHERE YEAR(o.created_at) = :year
        GROUP BY DATE_FORMAT(o.created_at, '%Y-%m'), p.name
        ORDER BY date
        """,
			nativeQuery = true)
	List<Object[]> getProductChartNative(@Param("year") int year);

	default List<ChartPointIntResponse> getProductChart(int year) {
		return getProductChartNative(year).stream()
				.map(row -> ChartPointIntResponse.builder()
						.date((String) row[0])
						.name((String) row[1])
						.value(row[2] != null ? Long.parseLong(row[2].toString()) : 0L)
						.year(year)
						.build())
				.toList();
	}
}
