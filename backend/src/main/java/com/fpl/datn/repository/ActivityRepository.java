package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.ActivityLog;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityLog, Integer> {}
