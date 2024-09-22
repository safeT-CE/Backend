package com.example.safeT.kickboard.entity;

import com.example.safeT.login.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "kickboard_id", nullable = false)
    private Long kickboardId;

    @Column(name = "penalty_id", nullable = false)
    private Long penaltyId;

    @Column(name = "rented_at", nullable = false)
    private LocalDateTime rentedAt;

    @Column(name = "returned", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean returned;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;
}
