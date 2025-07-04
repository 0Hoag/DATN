package com.fpl.datn.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fpl.datn.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL")
    Page<User> findAllActive(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL")
    List<User> findAllActive();

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL")
    Optional<User> findByIdAndNotDeleted(@Param("id") Integer id);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND " +
            "(" +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR u.phone LIKE CONCAT('%', :keyword, '%')" +
            ")")
    Page<User> findByEmailOrFullNameOrPhoneContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.deletedAt IS NULL AND " +
            "LOWER(r.name) = LOWER(:roleName)")
    Page<User> findByRoleName(@Param("roleName") String roleName, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.deletedAt IS NULL AND " +
            "LOWER(r.name) = LOWER(:roleName) AND " +
            "(" +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR u.phone LIKE CONCAT('%', :keyword, '%')" +
            ")")
    Page<User> findByKeywordAndRoleName(@Param("keyword") String keyword,
                                        @Param("roleName") String roleName,
                                        Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.deletedAt IS NOT NULL")
    Page<User> findDeletedUsers(Pageable pageable);
}