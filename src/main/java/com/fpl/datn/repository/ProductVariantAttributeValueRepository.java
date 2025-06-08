package com.fpl.datn.repository;

import com.fpl.datn.models.ProductVariantAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariantAttributeValueRepository extends JpaRepository<ProductVariantAttributeValue,Integer> {
}
