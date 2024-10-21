package com.example.kickboard.kickboard.entity;

import com.example.kickboard.login.entity.User;
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
    private Long duration; // 주행 시간 (분 단위)

    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "rental_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "rental_longitude"))
    })
    @Embedded
    private Location rentalLocation; // 대여 장소

    @AttributeOverrides({
            @AttributeOverride(name = "latitude", column = @Column(name = "return_latitude")),
            @AttributeOverride(name = "longitude", column = @Column(name = "return_longitude"))
    })
    private Location returnLocation; // 반납 장소

    // 지번 주소
    @Column(name = "rental_address")
    private String rentalAddress;

    @Column(name = "return_address")
    private String returnAddress;
}