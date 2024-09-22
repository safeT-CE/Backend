package com.example.safeT.kickboard.dto;

import com.example.safeT.kickboard.entity.Location;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalDetailDTO {
    private Long id;
    private Long kickboardId;
    private Long userId;
    private LocalDateTime rentedAt;
    private LocalDateTime returnedAt;
    private Boolean returned;

    private Long duration; // 주행 시간
    private Double distance; // 주행 거리
    private Location rentalLocation; // 대여 장소
    private Location returnLocation; // 반납 장소
}
