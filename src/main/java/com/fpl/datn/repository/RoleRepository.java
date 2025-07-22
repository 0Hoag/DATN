package com.fpl.datn.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Set<Role>> findAllByNameIn(Set<String> roles);
}
