package com.example.safeT.kickboard.entity;

import com.example.safeT.login.entity.User;
import com.example.safeT.kickboard.entity.PMap;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicInsert
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

    @Column(name = "duration")
    private Long duration; // 주행 시간 (초 단위)

    @Column(name = "distance", nullable = false)
    private double distance; // 주행 거리

    @Column(name = "rental_location")
    @Embedded
    private Location rentalLocation; // 대여 장소

    @Column(name = "return_location")
    @Embedded
    private Location returnLocation; // 반납 장소
}