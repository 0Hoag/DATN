package com.fpl.datn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ProductReview;

import feign.Param;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {
    // Tìm tất cả các bình luận gốc (không phải là reply)
    Page<ProductReview> findByParentReviewIsNull(Pageable pageable);

    // Kiểm tra xem người dùng đã đánh giá sản phẩm này chưa (chỉ cho bình luận gốc)
    boolean existsByUserIdAndProductId(Integer userId, Integer productId);

    @Query("SELECT AVG(r.rating) FROM ProductReview r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Integer productId);
}
