package com.fpl.datn.models;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer fpId;

    @Column(nullable = false)
    Integer otp;

    @Column(nullable = false)
    Date expirationTime;

    @OneToOne
    User user;
}
