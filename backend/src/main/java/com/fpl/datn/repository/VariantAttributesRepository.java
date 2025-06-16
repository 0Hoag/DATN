package com.fpl.datn.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.VariantAttribute;

@Repository
public interface VariantAttributesRepository extends JpaRepository<VariantAttribute, Integer> {
    Optional<VariantAttribute> findByName(String name);
}
