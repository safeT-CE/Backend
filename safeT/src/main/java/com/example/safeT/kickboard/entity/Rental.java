package com.example.safeT.kickboard.entity;

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

    @Column(name = "kickboard_id", nullable = false)
    private Long kickboardId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "penalty_id", nullable = false)
    private Long penaltyId;

    // 대여 시각
    @Column(name = "rented_at", nullable = false)
    private LocalDateTime rentedAt;

    // 반납 여부
    @Column(name = "returned", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean returned;

    // 반납 시각
    @Column(name = "returned_at")
    private LocalDateTime returnedAt;
}
