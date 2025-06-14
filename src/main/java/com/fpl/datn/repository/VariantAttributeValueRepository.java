package com.fpl.datn.repository;

import com.fpl.datn.models.VariantAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantAttributeValueRepository extends JpaRepository<VariantAttributeValue, Integer> {
}
