package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {}
