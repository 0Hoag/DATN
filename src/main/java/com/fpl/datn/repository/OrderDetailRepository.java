package com.fpl.datn.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpl.datn.dto.response.ChartPointIntResponse;
import com.fpl.datn.models.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    // Kiểm tra user đã mua sản phẩm chưa
    @Query("SELECT COUNT(od) > 0 FROM OrderDetail od " + "WHERE od.order.user.id = :userId "
            + "AND od.product.id = :productId "
            + "AND od.order.orderStatus = 'COMPLETED' "
            + "AND (od.order.isDelete = false OR od.order.isDelete IS NULL)")
    boolean hasUserPurchasedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

    @Query(
            """
			SELECT new com.fpl.datn.dto.response.ChartPointIntResponse(
				FUNCTION('MONTH', od.createdAt),
				SUM(od.quantity)
			)
			FROM OrderDetail od
			WHERE od.createdAt BETWEEN :startDate AND :endDate
			GROUP BY FUNCTION('MONTH', od.createdAt)
			ORDER BY FUNCTION('MONTH', od.createdAt)
			""")
    List<ChartPointIntResponse> getMonthlyProductSold(
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
