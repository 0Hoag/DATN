package com.fpl.datn.repository;

import com.fpl.datn.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    HashSet<Role> findAllByNameIn(Set<String> roles);
}
