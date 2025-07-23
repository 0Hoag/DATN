package com.fpl.datn.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpl.datn.dto.response.ChartPointIntResponse;
import com.fpl.datn.dto.response.TopProductResponse;
import com.fpl.datn.models.Order;

@Repository
public interface DashboardRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT COUNT(u.id) FROM User u")
    long countTotalUsers();

    @Query("SELECT COUNT(o) FROM Order o WHERE o.isDelete = false")
    long countTotalOrders();

    @Query("""
			SELECT SUM(od.quantity)
			FROM OrderDetail od
			JOIN od.order o
			WHERE o.isDelete = false
		""")
    long totalProductsSold();

    @Query(
            value =
                    """
		SELECT CONCAT(p.name, ' - ', pv.variant_name) AS product_name, p.thumbnail, SUM(pv.sold) AS quantity_sold, DATE_FORMAT(pv.created_at, '%m') AS date
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
                        .date((String) r[3])
                        .year(year)
                        .build())
                .toList();
    }

    @Query(
            """
			SELECT new com.fpl.datn.dto.response.ChartPointIntResponse(
				FUNCTION('MONTH', o.createdAt),
				COUNT(o.id)
			)
			FROM Order o
			WHERE o.createdAt BETWEEN :startDate AND :endDate
			AND o.isDelete = false
			GROUP BY FUNCTION('MONTH', o.createdAt)
			ORDER BY FUNCTION('MONTH', o.createdAt)
		""")
    List<ChartPointIntResponse> getMonthlyOrderCount(
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
