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

    // ===== ADMIN: XEM TẤT CẢ ĐÁNH GIÁ =====
    Page<ProductReview> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // ===== PUBLIC: XEM ĐÁNH GIÁ THEO SẢN PHẨM =====
    Page<ProductReview> findByProductIdAndIsVisibleTrueOrderByCreatedAtDesc(Integer productId, Pageable pageable);
    //    Page<ProductReview> findByProductIdOrderByCreatedAtDesc(Integer productId, Pageable pageable);

    // ===== KIỂM TRA USER ĐÃ ĐÁNH GIÁ SẢN PHẨM CHƯA =====
    boolean existsByUserIdAndProductId(Integer userId, Integer productId);

    @Query("SELECT pr FROM ProductReview pr " + "JOIN pr.product p "
            + "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :productName, '%')) "
            + "AND (pr.isVisible = true OR pr.isVisible IS NULL) "
            + "ORDER BY pr.createdAt DESC")
    Page<ProductReview> searchByProductName(@Param("productName") String productName, Pageable pageable);

    // Đếm tổng số đánh giá theo sản phẩm
    Long countByProductId(Integer productId);

    // Đếm số đánh giá theo sản phẩm và rating
    Long countByProductIdAndRating(Integer productId, Integer rating);

    // Tính rating trung bình theo sản phẩm
    @Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Integer productId);

    // ===== ADMIN: TÌM ĐÁNH GIÁ THEO USER =====
    Page<ProductReview> findByUserIdOrderByCreatedAtDesc(Integer userId, Pageable pageable);

    // ===== ADMIN: TÌM ĐÁNH GIÁ THEO RATING =====
    Page<ProductReview> findByRatingOrderByCreatedAtDesc(Integer rating, Pageable pageable);

    // ===== ADMIN: TÌM ĐÁNH GIÁ THEO SẢN PHẨM VÀ RATING =====
    Page<ProductReview> findByProductIdAndRatingOrderByCreatedAtDesc(
            Integer productId, Integer rating, Pageable pageable);
}
