package com.fpl.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {}
