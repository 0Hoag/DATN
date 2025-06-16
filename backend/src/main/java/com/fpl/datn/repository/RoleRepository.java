package com.fpl.datn.repository;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    HashSet<Role> findAllByNameIn(Set<String> roles);
}
