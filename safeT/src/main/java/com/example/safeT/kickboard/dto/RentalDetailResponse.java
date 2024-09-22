package com.example.safeT.kickboard.dto;

import com.example.safeT.kickboard.entity.PMap;
import lombok.Getter;
import lombok.Setter;

import java.tine.LocalDateTime;

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
    private PMap rentalLocation; // 대여 장소
    private PMap returnLocation; // 반납 장소
}
