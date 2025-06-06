package com.fpl.datn.repository;

import com.fpl.datn.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByEmail(String email);
}
