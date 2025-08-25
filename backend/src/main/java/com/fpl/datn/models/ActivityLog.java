package com.fpl.datn.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.fpl.datn.enums.ActionActicityLog;
import com.fpl.datn.enums.ActionActicityModule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private ActionActicityLog action;

    private String description;

    private ActionActicityModule module;

    private Integer objectID;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "user_action_id")
    private User userAction;
}
