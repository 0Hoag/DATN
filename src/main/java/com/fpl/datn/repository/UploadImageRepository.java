package com.fpl.datn.repository;

import com.fpl.datn.models.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadImageRepository extends JpaRepository<UploadImage, Integer> {
}