package com.fpl.datn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {

    Page<ProductReview> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<ProductReview> findByProductIdAndIsVisibleTrueOrderByCreatedAtDesc(Integer productId, Pageable pageable);

    boolean existsByUserIdAndProductId(Integer userId, Integer productId);

    @Query("SELECT pr FROM ProductReview pr " + "JOIN pr.product p "
            + "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) "
            + "AND (pr.isVisible = true OR pr.isVisible IS NULL) "
            + "ORDER BY pr.createdAt DESC")
    Page<ProductReview> searchByProductName(@Param("productName") String productName, Pageable pageable);

    // Phương thức đã được khôi phục
    @Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Integer productId);
}