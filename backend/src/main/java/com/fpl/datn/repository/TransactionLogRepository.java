package com.fpl.datn.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fpl.datn.dto.response.ChartPointResponse;
import com.fpl.datn.models.TransactionLog;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Integer> {
    TransactionLog findFirstByOrderIdAndActionTypeOrderByCreatedAtDesc(int oderId, String typeAction);

    Optional<TransactionLog> findByOrderIdAndActionTypeAndPaymentMethodId(
            Integer orderId, String actionType, Integer paymentMethodId);

    @Query(
            """
			SELECT SUM(t.amount)
			FROM TransactionLog t
			WHERE t.actionType = 'PAYMENT SUCCESS'
			OR t.status = 'RECEIED'
		""")
    BigDecimal getTotalRevenue();

    @Query(
            """
			SELECT new com.fpl.datn.dto.response.ChartPointResponse(
				FUNCTION('MONTH', t.createdAt),
				SUM(t.amount)
			)
			FROM TransactionLog t
			WHERE t.createdAt BETWEEN :startDate AND :endDate
			AND (t.actionType = 'PAYMENT SUCCESS' OR t.status = 'RECEIED')
			GROUP BY FUNCTION('MONTH', t.createdAt)
			ORDER BY FUNCTION('MONTH', t.createdAt)
			""")
    List<ChartPointResponse> getMonthlyRevenueOfYear(
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
