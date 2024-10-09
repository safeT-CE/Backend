package com.example.kickboard.kickboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class Location {

    // 위도
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    // 경도
    @Column(name = "longitude", nullable = false)
    private Double longitude;
}
