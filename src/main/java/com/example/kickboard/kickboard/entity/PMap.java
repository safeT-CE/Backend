package com.example.kickboard.kickboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Penalty Map 표시
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class PMap {

    // 위도
    @Column(name = "latitude", updatable= false)
    private Double latitude;

    // 경도
    @Column(name = "longitude", updatable= false)
    private Double longitude;
}
