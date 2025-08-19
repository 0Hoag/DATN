package com.fpl.datn.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Page<Voucher> findByIsActiveTrueAndStartAtBeforeAndEndAtAfterAndIdNotIn(
            LocalDateTime startAt, LocalDateTime endAt, List<Integer> ids, Pageable pageable);
    // Tìm voucher theo code
    Optional<Voucher> findByCode(String code);


    boolean existsByCode(String code);

    Page<Voucher> findByIsActiveTrueAndStartAtBeforeAndEndAtAfter(
            LocalDateTime start, LocalDateTime end, Pageable pageable);


    Page<Voucher> findByCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String code, String description, Pageable pageable);

    @Query(
            """
	SELECT v
	FROM Voucher v
	WHERE CURRENT_TIMESTAMP < v.endAt
	AND v.id NOT IN (
		SELECT uv.voucher.id
		FROM ZUserVoucher uv
		WHERE uv.user.id = :userId
	)
		""")
    Page<Voucher> findAvailableVouchers(@Param("userId") Integer userId, Pageable pageable);
}
