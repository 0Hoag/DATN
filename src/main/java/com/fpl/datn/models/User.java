package com.fpl.datn.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    @Column(name = "is_enable")
    private Boolean isEnable;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "staff_member")
    private Boolean staffMember;

    @Column(name = "is_guest")
    private Boolean isGuest;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ProductReview> productReviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> carts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OrderReturn> orderReturns;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ZUserVoucher> ZUserVouchers;

    @OneToMany(mappedBy = "userAction", cascade = CascadeType.ALL)
    private List<ActivityLog> activityLogs;
}
