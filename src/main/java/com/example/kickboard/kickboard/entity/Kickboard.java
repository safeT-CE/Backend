package com.example.kickboard.kickboard.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@Entity
@DynamicInsert
@Table(name = "kickboard")
public class Kickboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "qr_code", nullable = false, unique = true)
    private String qrCode;

    @Column(name = "model", nullable = false)
    private Long model;

    // 킥보드 현재 상태
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    // 킥보드 대여 여부
    @Column(name = "rented", nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean rented;

    // 킥보드 현재 위치
    @Column(name = "location", nullable = false)
    private String location;

    // 킥보드 배터리 상태
    @Column(name = "battery", nullable = false)
    private int battery;

    public enum Status {
        AVAILABLE,
        MAINTENANCE,
        OUT_OF_SERVICE
    }
}
