package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
